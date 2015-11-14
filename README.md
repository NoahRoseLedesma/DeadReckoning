# DeadReckoning
Dead reckoning application for Redshift Robotics 5619

Class controllerModule:

  	Example implementation of other classes.


Class InstructionSet:

  	Used as an extension to arrayList to hold instructions.

Values:

 	BufferedWriter output – Buffered Writer to write instruction packet to the filesystem

  	File outFile - File for instruction packet to be written to.

  	AtomicReference mainPacket – Reference to the globalInfoPacket instantiation, used to get session hash.

Methods:

  	Constructor – Requires an Atomic Reference to globalInfoPacket

  	exportInstructions – Writes instructions to the filesystem.



Class Instruction:

	Individual instruction object that holds movement data.

Values:

  	Boolean (primitive) motorFR, motorFL, motorBR, motorBL. - Info on whether the instruction set states a specific motor should move.

  	Int motorFRDest, motorFLDest, motorBRDest, motorBLDest. - Target destination from current position in millimeters.

Methods:

  	Constructor – Pass destination values for each of the motors to set their values. Or else don’t pass any arguments.

	setVales – set the motor destinations.



Class globalInfoPacket:

	Information about the motors and the wheels. Currently only supports a single wheel type.

Values:

  	Boolean (primitive) uncommitedChanges – A Boolean the represents whether motor or wheel values have been changed

  	Int sessionHash – A random integer to be written to the first line of both the global export and the instructionSet export. This key will ensure that the filesystem globalInfoPacket and instruction set exports are on the same version.

  	Int encoderCPR – The motor’s encoder CPR. To be set with setEncoderCPR method

  	Float gearRatio – The motor’s gear ratio. To be set with setGearRatio method

  	Float wheelCircumference – The circumference of the wheel attached to the motor. To be set with setWheelCircumference

  	Random ran – a random number generator for sessionHash generation. 

  	PrintWriter output – A PrintWriter to write globalInfoPacket to the filesystem

  	File outFile – the file that globalInfoPacket writes to.



Methods:

	Constructor – creates random number generator.

	commitChanges  - generates a new sessionHash for after motor and wheel values are changed

	exportPacket – exports the motor and wheel data to the filesystem. commitChanges must be called first.

	setEncoderCPR – sets the Encoder CPR

	setWheelCircumference – sets the Wheel Circumference 

	setGearRatio – sets the Gear Ratio