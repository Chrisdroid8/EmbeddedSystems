//*******************************************************************
/*!
\file   main.cpp
\author Gruppe Nimbus2000
\date   04.01.2026
\brief  Stoppuhr
*/

//*******************************************************************
#include <stdio.h>
#include <cmath> //für fmod() benötigt
#include "EmbSysLib.h"
#include "ReportHandler.h"
#include "config.h"

//*******************************************************************
/*
    LED-Belegung:
        LD1  rot     PJ13
        LD2  grün    PJ5

    Taster-Belegung:
        Btn1         PF8
        Btn2         PF9
        Btn3         PA6
        User         PA0
*/
//*******************************************************************

class App : public Timer::Task
{
public:
    // Zustände der Stoppuhr
    // CLEAR   : Zeit ist zurückgesetzt
    // RUNNING : Zeit läuft
    // STOPPED : Zeit ist angehalten
    enum State
    {
        CLEAR,
        RUNNING,
        STOPPED
    };

    // Konstruktor:
    // - ermittelt die Timer-Periode in Sekunden
    // - registriert die App als Task beim Timer
    App(Timer *timer)
    {
        T = timer->getCycleTime() * 1E-6f;
        timer->add(this);
    }

    // Wird zyklisch vom Timer aufgerufen
    // Erhöht den Zähler nur, wenn die Stoppuhr läuft
    void update()
    {
        if (state == RUNNING)
        {
            cnt++;
        }
    }

    // Startet die Stoppuhr
    // Erlaubt aus CLEAR oder STOPPED
    void start()
    {
        if (state == CLEAR || state == STOPPED)
        {
            state = RUNNING;
        }
    }

    // Stoppt die Stoppuhr
    // Nur erlaubt im Zustand RUNNING
    void stop()
    {
        if (state == RUNNING)
        {
            state = STOPPED;
        }
    }

    // Setzt die Stoppuhr zurück
    // Nur erlaubt im Zustand STOPPED
    void reset()
    {
        if (state == STOPPED)
        {
            cnt = 0;
            state = CLEAR;
        }
    }

    // Gibt die gemessene Zeit in Sekunden zurück
    float getTime()
    {
        return static_cast<float>(cnt) * T;
    }

private:
    DWORD cnt = 0;        // Anzahl der Timer-Ticks
    float T;              // Dauer eines Timer-Ticks in Sekunden
    State state = CLEAR;  // aktueller Zustand der Stoppuhr
};

//*******************************************************************

int main(void)
{
    // Deklaration: Statusvariablen für Warnmeldungen
    bool warningActive = false;   // Warnung wird angezeigt
    bool multiPressed  = false;   // Mehrere Taster gleichzeitig gedrückt

    // Deklaration: Parameter für die Balkenanzeige
    const int   maxBars = 50;     // Maximale Balkenlänge
    const float maxTime = 60.0f;  // Zeit für einen vollen Balken

    // Startanzeige auf dem LCD
    lcd.printf(0, 0, __DATE__ "," __TIME__);
    lcd.printf(1, 0, "beste Stoppuhr");
    lcd.refresh();

    // Applikation initialisieren und beim Timer anmelden
    App app(&timer);

    while (1)
    {
        // Tasterauswertung
        if (Btn1.get())
        {
            app.start();
        }

        if (Btn2.get())
        {
            app.stop();
        }

        if (Btn3.get())
        {
            app.reset();
        }

        // Zeitberechnung
        float totalSeconds = app.getTime();
        int minutes        = static_cast<int>(totalSeconds / 60);
        int seconds        = static_cast<int>(totalSeconds) % 60;
        int milliseconds   = static_cast<int>(
                                (totalSeconds - (minutes * 60 + seconds)) * 1000
                              );

        // Anzeige der Zeit
        lcd.printf(
            3,
            0,
            "Time: %02dmin %02ds %03dms",
            minutes,
            seconds,
            milliseconds
        );

        // Warnung bei gleichzeitiger Betätigung mehrerer Taster
        int pressed = Btn1.get() + Btn2.get() + Btn3.get();

        if (pressed > 1)
        {
            warningActive = true;
            multiPressed  = true;
            lcd.printf(8, 0, "WARNUNG: Mehrere Tasten gedrueckt!");
        }
        else
        {
            if (multiPressed && pressed == 1)
            {
                warningActive = false;
                multiPressed  = false;
                lcd.printf(8, 0, "                                      ");
            }
        }

        // Balkenanzeige zur Visualisierung der Zeit
        float timeForBar = fmod(app.getTime(), maxTime);
        int bars         = static_cast<int>((timeForBar / maxTime) * maxBars);

        if (bars > maxBars)
        {
            bars = maxBars;
        }

        char line[maxBars + 1];

        for (int i = 0; i < bars; i++)
        {
            line[i] = '|';
        }

        for (int i = bars; i < maxBars; i++)
        {
            line[i] = ' ';
        }

        line[maxBars] = '\0';

        lcd.printf(4, 0, "%s", line);

        // LCD aktualisieren
        lcd.refresh();
    }
}
