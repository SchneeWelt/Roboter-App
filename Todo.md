# Todos

- Es wäre Wichtig den übermittelten Wert für die Fahrgeschwindigkeit zwischen -1 und 1 zu halten. Aktuell muss 
da ein Wert zwischen -100 und 100 gesendet werden, einfach, weil die Roboterseite so programmiert ist. Die müsste
dann bei einer Änderung ebenfalls angepasst werden. Die entsprechende Stelle ist im SteeringController der Arduino
Software zu finden. 

<- Achtung beim senden der Steuerdaten gibt es eine Deadzone, die ist fest als 5 gesetzt. Der Wert müsste also auch
angepasst werden!