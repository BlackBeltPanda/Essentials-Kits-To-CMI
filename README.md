# Essentials-Kits-To-CMI
Converts Essentials kits to CMI kits

I whipped up this plugin because I had 80 different kits to convert from Essentials to CMI and CMI doesn't have the ability to do that built-in.
No configuration, no commands, just follow these steps to convert your kits:

1. Make sure BOTH Essentials and CMI are present on the server (in the plugins folder)
2. Drop EssentialsKitsToCMI.jar into the plugins folder and start/restart the server.
3. Kits should automatically be converted once the server's done starting up. Verify with "/cmi kit" or by checking CMI's kits.yml file. The kits.yml file may not be updated until you run "/cmi reload".
4. Stop the server and remove EssentialsKitsToCMI.jar from the plugins folder. If you're done with it, you can also remove the Essentials .jar.
5. Start the server and you're good to go.

Notes:
- This plugin tries to convert everything it can, but if you have an item that Essentials failed to convert from pre-1.13 names to 1.13+ names, this plugin won't be able to convert it to CMI. Same goes for custom books if the chapters are missing from Essentials. If any items are missing, you'll have to add them manually.
- It's assumed that all kits are enabled, the Command Name is the same as the kit name, and that if the delay is less than 0 then it's a 1-time use kit.
- This plugin does not do anything with permissions, so you'll need to convert from Essentials kit permissions to CMI kit permissions yourself.
- It's recommended to go through each kit and make sure all the settings are to your liking. For example, the kit icon is always the first item in the kit, so you may want to change that.
