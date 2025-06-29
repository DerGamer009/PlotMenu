# PlotMenu

Simple PlotSquared GUI menu for Minecraft 1.21 - 1.21.6 servers.
The menu provides quick access to common plot actions such as claiming,
teleporting home, managing members and more.

## Building

This project uses Maven. To build the plugin run:

```bash
mvn package
```

The resulting jar will be in `target/`.

## Usage

Place the jar in your server's `plugins` folder and start the server. Use `/plotmenu` in game to open the GUI. A default `config.yml` will
be generated on first run which allows you to customise the menu title and item names. Reload the configuration with `/plotmenu reload`.
