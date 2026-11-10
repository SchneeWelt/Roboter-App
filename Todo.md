# Todos

-       Der IP Button macht gar keinen Sinn, da ich ja nicht IP Adressen direkt verwendet
        sondern mDNS. Müsste ich mal anpassen.

-       Ich sollte diese Datei löschen und die TODO Spalte als ## in die Readme packen

-       Statt Bluetooth Device muss ich ein eigenes Device Klasse bauen. Dann funzt die 
        architektur auch über WLan. 

-       OscProtokoll zum Senden der Daten verwenden. Habe für Software Engineering 
        eine OSC Library gebaut. Die könnte ich verwenden. 

-       Die UI Elemente Klasse muss weg. Das Verhalten der Widgets muss in den 
        Widgets selbst implementiert werden. Es müssen Klassen so gestaltet werden,
        dass die Kommunikation zu anderen Objekten überflüssig wird. 
  
        In AppCompatButton oder einem anderen Widget: getContext() und dann Cast auf
        MainActivity. So kommt man an wichtige anderen Klassen rann!
        

-       Der IP Button ist eigentlich quatsch. Wenn der W-Lan modus aktiv ist und 
        der W-Lan handler keine IP Adresse und keinen Port gespeichert hat, dann
        muss beim betätigen des connect buttons das Pop Up geöffnet werden, welches
        aktuell der IP Button öffnet. Den IP Button brauche ich also gar nicht