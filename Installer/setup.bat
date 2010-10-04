mkdir "%ProgramFiles%\Team Online"
mkdir "%ProgramFiles%\Team Online\Downloadinator"
mkdir "%ALLUSERSPROFILE%\Microsoft\Windows\Start Menu\Programs\Team Online"
mkdir "%ALLUSERSPROFILE%\Microsoft\Windows\Start Menu\Programs\Team Online\Downloadinator"

move bin "%ProgramFiles%\Team Online\Downloadinator\"
move conf "%ProgramFiles%\Team Online\Downloadinator\"
move lib "%ProgramFiles%\Team Online\Downloadinator\"
move logs "%ProgramFiles%\Team Online\Downloadinator\"
move target "%ProgramFiles%\Team Online\Downloadinator\"
move "Installer\Uninstall Downloadinator Service.lnk" "%ALLUSERSPROFILE%\Microsoft\Windows\Start Menu\Programs\Team Online\Downloadinator\"

cd "%ProgramFiles%\Team Online\Downloadinator\"

call bin\InstallService.bat
call bin\StartService.bat
