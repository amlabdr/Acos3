************************************************************************************************************************
***************The objective of this project is to create an applet inspired by the card ACOS3 from ACS.***************

********************************@author ABDERRAHIM AMALOU & ZINEB EL KACIMI********************************

------------------------------------------------------------------------------------------------------------------------------------------------
________________________________________________________________________________________________________________________
											     |
In this folder:										     |
	-  The project									     |
	-  testScripts folder is for testing all instruction of this projects.				     |
	-  README.txt description of the project							     |
											     |
________________________________________________________________________________________________________________________|

In this applet you can perform like an ACOS3 card by sending APDU corresponding to some instructions defined in this 
applet.

****************************************************HOW IT WORKS ?***************************************************

1- Instruction "insSelect" 0x4A
	There is a vector called selectedFile[4] that contain 4 bytes:
		two first bytes for the id of the file to select
		selectedFile[2] for the index of the file in FF04 //very useful inside the program
		selectedFile[3] for the position of file in memory (memory is a vector of bytes that contain all
						                  files that we create)
	Actually this instruction corresponds to update values in this vector to values of current file  to select. So we
	have the information about selected file every time we want in the program.

2- Instruction "insSubmit" 0x02
	By executing the function SubmitF(APDU) we can check our codePin or our codeIc submitted in the data of 
	the APDU.
	The function check the value of offset P1 to identify is the user want to submit the codePin or the icCode?!
	0x07 for codeIc and 0x06 for codePin

3- Instruction insChangeCode 0x03
	With this instruction you can change current codePin or codeIc but first you should submit them correctly
	Also this function test the offset P1 to identify is the user want to change the codePin or the icCode?!
	0x07 for codeIc and 0x06 for codePin

4- Instruction insWrite 0xD0
	With this instruction you can either create new files or update existing ones but first you should verify security
	conditions that mentioned in the file FF04.
	The creation of a file pass by three steps:
		1- Creating or updating the record of this file in the FF04 (specify the lenght and number of records 
		and security conditions for write and read and finally the Id of the file in two bytes). All this 
		must be sent in the APDU.offset.data and the parameter P1 must equals to 0x01.
		2- Selecting the new file by sending the Id of file in the APDU.offset.data and using "insSelect".
		3- Write or update the file in memory respecting security conditions that mentioned in the file FF04.
		
5- Instruction insRead 0xB2
	With this instruction you can read any part of any file while you satisfied security conditions mentioned in FF04.
	offset.P1 and offset.P2 are used for the offsetLow and offsetHigh. for example :
	after selecting FF04  with this  APDU: 0x80 0xB2 0x00 0x08 0x00  0x7F;  you read only data of the file FF04 from 
	the P1 byte to P2 byte in this example 8 first bytes.

6- Instruction insClear 0x04
	With this instruction you can clear all files (not system files) and all security codes and memory.

