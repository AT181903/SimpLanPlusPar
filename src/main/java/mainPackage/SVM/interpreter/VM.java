package mainPackage.SVM.interpreter;

import mainPackage.SVM.ast.Instruction;
import mainPackage.SimpLanPlus.utils.errors.CustomException;

import java.util.*;

public class VM {
    private final ArrayList<Instruction> code;

    public static final int MEMSIZE = 1000;

    private final MemoryItem[] memory;

    // Registers
    private int programCounter; // Program Counter
    private int stackPointer; // Stack Pointer
    private int heapPointer; // Heap Pointer
    private int framePointer; // Frame pointer
    private int returnAddress; // Return address
    private int accessLink; // Access link
    private int a0; // Service address
    private int t1; // Tmp address

    public VM(ArrayList<Instruction> code) {
        this.code = code;

        memory = new MemoryItem[MEMSIZE];

        for (int i = 0; i < MEMSIZE; i++) {
            memory[i] = new MemoryItem();
        }

        // Init registers
        programCounter = 0;
        stackPointer = MEMSIZE;
        framePointer = MEMSIZE;
    }

    public void execute() {
        while (true) {

            // Check for memory availability
            if (getMemoryStatus() > stackPointer) {
                new CustomException("Out of memory");
                return;
            } else {
                Instruction instruction = code.get(programCounter);
                programCounter++;
                String param1 = instruction.getParam1();
                String param2 = instruction.getParam2();
                String param3 = instruction.getParam3();
                Integer offset = instruction.getOffset();

                switch (instruction.getName()) {
                    case "push":
                        stackPointer = stackPointer - 1;
                        memory[stackPointer].setData(readRegister(instruction.getParam1()));
                        break;
                    case "pop":
                        stackPointer = stackPointer + 1;
                        break;
                    case "add":
                        writeRegister(param1, readRegister(param2) + readRegister(param3));
                        break;
                    case "mult":
                        writeRegister(param1, readRegister(param2) * readRegister(param3));
                        break;
                    case "div":
                        Integer dividerDiv = readRegister(param3);
                        if (dividerDiv != 0) {
                            writeRegister(param1, readRegister(param2) / dividerDiv);
                        } else {
                            new CustomException("You cannot divide by 0");
                        }
                        break;
                    case "sub":
                        writeRegister(param1, readRegister(param2) - readRegister(param3));
                        break;
                    case "addi":
                        writeRegister(param1, readRegister(param2) + Integer.parseInt(param3));
                        break;
                    case "subi":
                        writeRegister(param1, readRegister(param2) - Integer.parseInt(param3));
                        break;
                    case "multi":
                        writeRegister(param1, readRegister(param2) * Integer.parseInt(param3));
                        break;
                    case "divi":
                        Integer dividerDivi = Integer.parseInt(param3);

                        if (dividerDivi != 0) {
                            writeRegister(param1, readRegister(param2) / dividerDivi);
                        } else {
                            new CustomException("You cannot divide by 0");
                        }

                        break;
                    case "lw":
                        if (memory[readRegister(param2) + offset].isFree()) {
                            new CustomException("You cannot load null values");
                        } else {
                            writeRegister(param1, memory[readRegister(param2) + offset].getData());
                        }
                        break;
                    case "sw":
                        if (readRegister(param1) != null) {
                            memory[readRegister(param2) + offset].setData(readRegister(param1));
                        } else {
                            new CustomException("You cannot store null values");
                        }
                        break;
                    case "b":
                        programCounter = Integer.parseInt(param1);
                        break;
                    case "beq":
                        if (Objects.equals(readRegister(param1), readRegister(param2))) {
                            programCounter = Integer.parseInt(param3);
                        }
                        break;
                    case "bleq":
                        if (readRegister(param1) <= readRegister(param2)) {
                            programCounter = Integer.parseInt(param3);
                        }
                        break;
                    case "print":
                        String programOutput = "[PROGRAM OUTPUT]: " + readRegister(param1);
                        System.err.println("\u001B[32m" + programOutput + "\u001B[0m");
                        break;
                    case "and":
                        Boolean input1And = readRegister(param2) == 1;
                        Boolean input2And = readRegister(param3) == 1;
                        Integer resultAnd = input1And && input2And ? 1 : 0;
                        writeRegister(param1, resultAnd);
                        break;
                    case "or":
                        Boolean input1Or = readRegister(param2) == 1;
                        Boolean input2Or = readRegister(param3) == 1;
                        Integer resultOr = input1Or || input2Or ? 1 : 0;
                        writeRegister(param1, resultOr);
                        break;
                    case "not":
                        Integer resultNot = readRegister(param2) == 1 ? 0 : 1;
                        writeRegister(param1, resultNot);
                        break;
                    case "li":
                        writeRegister(param1, Integer.parseInt(param2));
                        break;
                    case "mv":
                        writeRegister(param1, readRegister(param2));
                        break;
                    case "jal":
                        writeRegister("$ra", programCounter);
                        programCounter = Integer.parseInt(param1);
                        break;
                    case "jr":
                        programCounter = readRegister(param1);
                        break;
                    case "halt":
                        return;
                }
            }
        }
    }

    private Integer readRegister(String registerName) {
        switch (registerName) {
            case "$sp":
                return stackPointer;
            case "$hp":
                return heapPointer;
            case "$fp":
                return framePointer;
            case "$ra":
                return returnAddress;
            case "$a0":
                return a0;
            case "$t1":
                return t1;
            case "$al":
                return accessLink;
            default:
                return null;
        }
    }

    private void writeRegister(String registerName, Integer val) {

        switch (registerName) {
            case "$sp":
                stackPointer = val;
                break;
            case "$hp":
                heapPointer = val;
                break;
            case "$fp":
                framePointer = val;
                break;
            case "$ra":
                returnAddress = val;
                break;
            case "$a0":
                a0 = val;
                break;
            case "$t1":
                t1 = val;
                break;
            case "$al":
                accessLink = val;
                break;
        }

    }

    // Return MEMSIZE if reached the end of memory, so nothing is free
    private Integer getMemoryStatus() {
        Optional<MemoryItem> firstFreeMemoryCell = Arrays.stream(memory).filter(MemoryItem::isFree).findFirst();

        if (firstFreeMemoryCell.isPresent()) {
            for (int i = 0; i < MEMSIZE; i++) {
                if (memory[i] == firstFreeMemoryCell.get()) {
                    return i;
                }
            }
        }

        return MEMSIZE;
    }

}
