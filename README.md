Messenger
=========

Ziel
----

Dieses Minecraft-Bukkit-Plugin dient zum Versenden von Nachrichten an Spieler die Offline sind. 
Es funktioniert mit Kommandos im Chat. Nach dem Einloggen erhält der jeweilige Spieler die für ihn bestimmten
Nachrichten.

Vorraussetzungen
----------------
* Es muss das Minecraft-Bukkit-Mahn42-Framework Plugin installiert sein.

Kommandos
---------

* messenger_send @[Spielername] [Farbcode][Nachricht] = Sendet eine Nachricht an einen konkreten Spieler.
* messenger_send @all [Farbcode][Nachricht]           = Sendet eine Nachricht an alle registrierten Spieler.
* messenger_recall @[Spielername]                     = Ruft alle Nachrichten für einen konkreten Spieler zurück.
* messenger_recall @all                               = Ruft alle Nachrichten für alle registrierten Spieler zurück.

Farbcodes
---------
* &1 = Blau
* &2 = Rot
* &3 = Grün
* &4 = Gelb
* &5 = Gold
* &6 = Weiss
* &7 = Grau
* &8 = Aqua
* &9 = Violett

Konfiguration
-------------
In der config.yml kann man folgende Werte hinterlegen:
* MaxMessages = Maximale Anzahl von Nachrichten die ein Spieler mit einmal erhalten kann.

Ideen
-----
* Gruppen definieren, welche Nachrichten erhalten können.