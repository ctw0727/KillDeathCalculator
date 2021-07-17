Set shellObj = CreateObject("Wscript.Shell")
Dim command
command = "cmd /c KDAcalculator.bat"
shellObj.Run command, 0, false