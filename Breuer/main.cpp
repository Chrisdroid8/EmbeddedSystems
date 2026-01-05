//*******************************************************************
/*!
\file   main.cpp
\author Ina Cirpka
\date   04.01.2026
\brief  Stoppuhr
*/

//*******************************************************************
#include <stdio.h>
#include <cmath> //für fmod() benötigt
#include "EmbSysLib.h"
//-------------------------------------------------------------------
#include "ReportHandler.h"
#include "config.h"

//*******************************************************************
/*
  LED:
    LD1  red   PJ13
    LD2 green  PJ5

  Button:
    Btn1       PF8
    Btn2       PF9
    Btn3       PA6
    User       PA0

*/
//*******************************************************************

//*******************************************************************

class App : public Timer::Task
{
	public:
	//--------------------------------------------------------------
	enum State
	{
		CLEAR,
		RUNNING,
		STOPPED
	};


    //---------------------------------------------------------------
    App( Timer *timer )
    {
      T = timer->getCycleTime()*1E-6;
      timer->add( this );
    }

    //---------------------------------------------------------------
    void update()
    {
      if(state == RUNNING)
		{
			cnt++;
		}
    }
    //--------------------------------------------------------------

    void start()
    {
    	if(state == CLEAR)
    		state = RUNNING;
    	else if(state == STOPPED)
    		state = RUNNING;
    }

    //--------------------------------------------------------------

    void stop()
    {
    	if(state == RUNNING)
        	state = STOPPED;
    }

    //--------------------------------------------------------------

    void reset()
    {
        if(state == STOPPED)
        {
        	cnt = 0;
        	state = CLEAR;
        }
    }

    //---------------------------------------------------------------
    float getTime()
    {
      return( (float)cnt*T );
    }

	private:
    //---------------------------------------------------------------
    DWORD cnt = 0;
    float T;
    State state = CLEAR;
};

//*******************************************************************

int main(void)
{
  //Deklaration Warnungen
  bool warningActive = false;    // Warnung wird gerade angezeigt
  bool multiPressed = false;     // Es wurde mehrere Tasten gleichzeitig gedrückt
  //Deklaration für Visualisierung Balken
  const int maxBars = 50;   // Maximale Balkenlänge (Anzahl Zeichen)
      const float maxTime = 60; // 60 Sekunden für kompletten Balken

  lcd.printf( 0, 0, __DATE__ "," __TIME__ );
  lcd.printf( 1, 0, "Beste Stoppuhr" );
  lcd.refresh();
  App app( &timer );

  while(1)
  {
	  //Tastersteuerung
	  if(Btn1.get())
		  app.start();

	  if(Btn2.get())
		  app.stop();

	  if(Btn3.get())
		  app.reset();

	  //Zeitanzeige
	  float totalSeconds = app.getTime();        // Gesamtzeit in Sekunden
	  int minutes = (int)(totalSeconds / 60);   // ganze Minuten
	  int seconds = (int)(totalSeconds) % 60;   // Rest-Sekunden
	  int milliseconds = (int)((totalSeconds - (minutes*60 + seconds)) * 1000); // Rest in ms
	  lcd.printf(3, 0, "Time: %02dmin %02ds %03dms", minutes, seconds, milliseconds);

	  /*
	  //LEDs leuchten bei Betätigung
	  LD1.set( Btn1.get() );
	  LD2.set( Btn2.get() );

	  //Tasterinput als boolean anzeigen
	  lcd.printf( 4, 0, "User:%d", User.get() );
	  lcd.printf( 5, 0, "Btn1:%d", Btn1.get() );
	  lcd.printf( 6, 0, "Btn2:%d", Btn2.get() );
	  lcd.printf( 7, 0, "Btn3:%d", Btn3.get() );
	  */
	  // ---- Warnung bei mehreren gedrückten Tasten ----
	  int pressed = Btn1.get() + Btn2.get() + Btn3.get();

	  if(pressed > 1)
	  {
	      // Mehrere Tasten gleichzeitig -> Warnung aktivieren
	      warningActive = true;
	      multiPressed = true;
	      lcd.printf(8, 0, "WARNUNG: Mehrere Tasten gedrueckt!");
	  }
	  else
	  {
	      // Nur eine Taste gedrückt
	      if(multiPressed && pressed == 1)
	      {
	          // Ein weiterer Knopf wurde gedrückt -> Warnung löschen
	          warningActive = false;
	          multiPressed = false;
	          lcd.printf(8, 0, "                                      "); // Warnung löschen
	      }
	  }
	  //Visualisierung
	  float timeForBar = fmod(app.getTime(), maxTime);
	  int bars = (int)((timeForBar / maxTime) * maxBars);
	          if(bars > maxBars) bars = maxBars;

	          char line[maxBars+1];  // maxBars Zeichen + Nullterminator
	                  for(int i=0; i<bars; i++) line[i]='|';
	                  for(int i=bars; i<maxBars; i++) line[i]='                                  ';
	                  line[maxBars] = '\0';

	                  lcd.printf(4, 0, "%s", line); // Balkenzeile ausgeben
	  lcd.refresh(); //Bildschirm aktualisieren
  }

  //visulaisierung
}
