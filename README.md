# TranslateFont-Expansion
This is a [PlaceholderAPI](https://links.alonsoaliaga.com/PlaceholderAPI) expansion to allow owners/configurator customize texts a bit more.

# Installation
You can install this expansion in 2 ways:
### 1) PlaceholderAPI eCloud (Pending approval ✔️)
While being in console or having OP run the following commands:
> /papi ecloud download translatefont\
> /papi reload

✅ Expansion is ready to be used!
### 2) Manual download
Go to [eCloud](https://api.extendedclip.com/expansions/translatefont/) and click `Download Latest` button to get the .jar file.\
Copy and paste the file in `/plugins/PlaceholderAPI/expansions/` and run:
> /papi reload

✅ Expansion is ready to be used!
# Placeholders
The following placeholders are available:
## %translatefont_to_FONT-IDENTIFIER_YOUR-MESSAGE%
Allows you to translate your input (YOUR-MESSAGE) to the specified font (FONT-IDENTIFER).<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %translatefont_to_small-caps_&5&lWelcome to the server {player_name}!%<br>
**Output #1:** `§5§lᴡᴇʟᴄᴏᴍᴇ ᴛᴏ ᴛʜᴇ sᴇʀᴠᴇʀ ᴀʟᴏɴsᴏᴀʟɪᴀɢᴀ!`

**Example #2:** %translatefont_to_spaced_&5&lWelcome to the server {player_name}!%<br>
**Output #2:** `§5§lＷｅｌｃｏｍｅ ｔｏ ｔｈｅ ｓｅｒｖｅｒ ＡｌｏｎｓｏＡｌｉａｇａ!`

## %translatefont_tounparsed_FONT-IDENTIFIER_YOUR-MESSAGE%
Allows you to translate your input (YOUR-MESSAGE) to the specified font (FONT-IDENTIFER).<br>
This placeholder will not parse color codes. If found will be reverted to specified format in settings.<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %translatefont_to_small-caps_&5&lWelcome to the server {player_name}!%<br>
**Output #1:** `&5&lᴡᴇʟᴄᴏᴍᴇ ᴛᴏ ᴛʜᴇ sᴇʀᴠᴇʀ ᴀʟᴏɴsᴏᴀʟɪᴀɢᴀ!`

**Example #2:** %translatefont_to_spaced_&5&lWelcome to the server {player_name}!%<br>
**Output #2:** `&5&lＷｅｌｃｏｍｅ ｔｏ ｔｈｅ ｓｅｒｖｅｒ ＡｌｏｎｓｏＡｌｉａｇａ!`

# How to add new fonts?
To add new fonts you must go to `/plugins/PlaceholderAPI/config.yml` and search `translatefont` section.<br>
You can create a unique identifier (lowercase and dashes only) and start adding your characters to search and<br>
characters to replace. Remember both must be the same length.<br>
```yaml
expansions:                                                   #This exists already by default.
  translatefont:                                              #This exists already by default.
    formats:                                                  #This exists already by default.
      my-custom-font:                                         #You create this new section.
        to-search: "abcdefghijklmnñopqrstuvwxyz0123456789-+"  #The characters to search
        to-replace: "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴñᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ₀₁₂₃₄₅₆₇₈₉₋₊" #The characters that will replace them
```

# Want more cool and useful expansions?
<p align="center">
    <a href="https://alonsoaliaga.com/moregradients">MoreGradients Expansion</a><br>
    Create gradients easily for your texts with hex support!<br>
    <br>
    <a href="https://alonsoaliaga.com/tempdata">TempData Expansion</a><br>
    Store in cache temporary global or per player data to use where you need!<br>
    <br>
    <a href="https://alonsoaliaga.com/capitalize">Capitalize Expansion</a><br>
    Customize texts a bit more removing underscores, dashes and capitalizing letters!<br>
    <br>
    <a href="https://alonsoaliaga.com/checkmoney">CheckMoney Expansion</a><br>
    Check if player has enough funds or not with custom output! (Specially for menu plugins)<br>
    <br>
    <a href="https://alonsoaliaga.com/checkdate">CheckDate Expansion</a><br>
    Check if server/machine date is the desired one with custom output! (Specially for messages)<br>
</p>

# Want more tools?
**Make sure to check our [BRAND NEW TOOL](https://alonsoaliaga.com/hex) to generate your own text with gradients!**<br>
<p align="center">
    <a href="https://alonsoaliaga.com/hex"><img src="https://i.imgur.com/766Es8I.png" alt="Our brand new tool!"/></a>
</p>

# Do you have a cool expansion idea?
<p align="center">
    <a href="https://alonsoaliaga.com/discord">Join us on Discord</a><br>
    <a href="https://alonsoaliaga.com/discord"><img src="https://i.imgur.com/2pslxIN.png"></a><br>
    Let us know what's your idea and it might become true!
</p>

# Questions?
<p align="center">
    <a href="https://alonsoaliaga.com/discord"><img style="width:200px;" src="https://i.imgur.com/laEFHcG.gif"></a><br>
    <a href="https://alonsoaliaga.com/discord"><span style="font-size:28px;font-weight:bold;color:rgb(100,100,255);">Join us in our discord!</span></a>
</p>