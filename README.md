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

* messenger_send @[Spielername,Gruppe] [Farbcode][Nachricht] = Sendet eine Nachricht an einen Spieler oder eine Gruppe.
* messenger_send @all [Farbcode][Nachricht]                  = Sendet eine Nachricht an alle registrierten Spieler.
* messenger_recall @[Spielername,Gruppe]                     = Ruft alle Nachrichten für einen Spieler oder eine Gruppe zurück.
* messenger_recall @all                                      = Ruft alle Nachrichten für alle registrierten Spieler zurück.
* messenger_listgroups                                       = Listet alle Gruppen auf.
* messenger_listgroupusers [Gruppe]                          = Listet die in einer Gruppe enthaltenen Spieler auf.

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

Gruppen
-------
In der groups.yml können verschiedene Gruppen und deren zugehörigen Spieler eingetragen werden.
Ein Beispiel hierfür ist:

    Groups:
     - Name: ABC
       Users:
         - player1
         - player2
     - Name: DEF
       Users:
         - player1
     - Name: GHI
       Users:
         - player2