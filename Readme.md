# Projektdokumentation und Hinweise

## Roboter

Der Roboter besteht aus 2 Komponenten. Einmal ist dort dem Empfänger, ein ESP32 auf dem aktuell die WLan Schnittstelle
installiert ist und dann ist da der Arduino. Er übernimmt die Steuerung des Roboters indem er Befehle vom ESP32 über
UART entgegen nimt.

## Implementierungshinweis
Vermutlich wäre es sinnvoll zum Senden der Daten auf OSC umzusteigen - also noch eine weitere Schicht zu integrieren
und zu definieren, dass meine Steuerdaten nur über das OSC Protokoll gesendet werden können. Zum Senden bei Android
gibt es die JavaOSC Library.


ACHTUNG: WLan Modul auf ESP32 ist veraltet, da jetzt mDNS verwendet werden soll!