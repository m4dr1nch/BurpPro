## BurpSuite Pro Loader
### Installation
Download and unzip the archive [release](https://github.com/m4dr1nch/BurpPro/releases/tag/loader-3.2):
```bash
unzip BurpSuite-2022-3.2.zip
cd BurpSuite-2022/
```

Run the loader and complete the process:
```bash
./loader.jar
```

Once done you can create a binary launcher:
```
vim /usr/bin/burp
```

Input the following lines. The loader location is the loacation of the "loader.jar". The launch command is shown in the activation process:
```bash
#!/bin/bash
cd <LOADER_LOCATION> && <LAUNCH_COMMAND>
```
