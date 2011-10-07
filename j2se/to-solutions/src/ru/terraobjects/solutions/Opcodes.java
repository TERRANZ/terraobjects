package ru.terraobjects.solutions;

/**
 *
 * @author korostelev
 */
public interface Opcodes
{
    //Client opcodes

    public final int C_OPCODE_END = 0;
    public final int C_OPCODE_INSERT_HASH = 1;
    public final int C_OPCODE_GET_HASHES = 2;
    public final int C_OPCODE_CREATE_OBJ = 10;
    public final int C_OPCODE_SET_PROP = 11;
    public final int C_OPCODE_GET_OBJ = 20;
    public final int C_OPCODE_GET_PROP = 21;
    public final int C_OPCODE_GET_OBJ_PROPS = 22;
    public final int C_OPCODE_CLEAN_FULL = 100;
    public final int C_OPCODE_REM_OBJ = 101;
    public final int C_OPCODE_REM_OBJ_BY_TEMPL = 102;
    //Server opcodes
    public final int S_OPCODE_OK = 0;
    public final int S_OPCODE_ERR = 1;
    public final int S_OPCODE_OBJ = 10;
    public final int S_OPCODE_PROP = 11;
}
