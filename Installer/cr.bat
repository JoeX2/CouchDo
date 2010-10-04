del target\DownloadinatorSetup.7z
del target\DownloadinatorSetup.exe

cd ..

Installer\7zr a target\DownloadinatorSetup.7z bin\InstallService.bat
Installer\7zr a target\DownloadinatorSetup.7z bin\UninstallService.bat
Installer\7zr a target\DownloadinatorSetup.7z bin\StartService.bat
Installer\7zr a target\DownloadinatorSetup.7z bin\Test.bat
Installer\7zr a target\DownloadinatorSetup.7z bin\wrapper-windows-x86-32.exe
Installer\7zr a target\DownloadinatorSetup.7z target\Downloadinator-0.1-jar-with-dependencies.jar
Installer\7zr a target\DownloadinatorSetup.7z conf\wrapper.conf
Installer\7zr a target\DownloadinatorSetup.7z lib\wrapper-windows-x86-32.dll
Installer\7zr a target\DownloadinatorSetup.7z logs\
Installer\7zr a target\DownloadinatorSetup.7z Installer\setup.bat
Installer\7zr a target\DownloadinatorSetup.7z "Installer\Uninstall Downloadinator Service.lnk"

copy /b Installer\7zSD.sfx + Installer\config.txt + target\DownloadinatorSetup.7z target\DownloadinatorSetup.exe
