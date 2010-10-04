#!/bin/bash

cd ..

rm target/DownloadinatorSetup.7z
rm target/DownloadinatorSetup.exe

7zr a target/DownloadinatorSetup.7z bin/InstallService.bat
7zr a target/DownloadinatorSetup.7z bin/UninstallService.bat
7zr a target/DownloadinatorSetup.7z bin7StartService.bat
7zr a target/DownloadinatorSetup.7z bin/Test.bat
7zr a target/DownloadinatorSetup.7z bin/wrapper-windows-x86-32.exe
7zr a target/DownloadinatorSetup.7z target/Downloadinator-0.1-jar-with-dependencies.jar
7zr a target/DownloadinatorSetup.7z conf/wrapper.conf
7zr a target/DownloadinatorSetup.7z lib/wrapper-windows-x86-32.dll
7zr a target/DownloadinatorSetup.7z logs/
7zr a target/DownloadinatorSetup.7z Installer/setup.bat
7zr a target/DownloadinatorSetup.7z Installer/Uninstall\ Downloadinator\ Service.lnk

cat Installer/7zSD.sfx Installer/config.txt target/DownloadinatorSetup.7z > target/DownloadinatorSetup.exe
