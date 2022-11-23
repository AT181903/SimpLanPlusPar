package mainPackage.SimpLanPlus.utils.symbol_table;

import mainPackage.SimpLanPlus.utils.errors.CustomError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SymbolTable {
    private ArrayList<HashMap<String, SymbolTableEntry>> symbolTable = new ArrayList<>();

    private int nestingLevel = -1;

    private int offset;

    public ArrayList<HashMap<String, SymbolTableEntry>> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(ArrayList<HashMap<String, SymbolTableEntry>> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void newScope() {
        offset = 0;
        nestingLevel++;
    }

    public void incrementOffset() {
        offset++;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public HashMap<String, SymbolTableEntry> getSymbolTableAtCurrentLevel() {
        return symbolTable.get(this.nestingLevel);
    }

    public HashMap<String, SymbolTableEntry> getSymbolTableAtPreviousLevel() {
        int previousLevel = this.nestingLevel - 1;
        if (previousLevel >= 0) {
            return symbolTable.get(previousLevel);
        } else {
            return symbolTable.get(this.nestingLevel);
        }
    }

    public int getOffset() {
        return offset;
    }

    public void addToST(HashMap<String, SymbolTableEntry> symbolTableEntryHashMap) {
        symbolTable.add(symbolTableEntryHashMap);
    }

    public SymbolTableEntry lookup(String id) {
        for (int level = nestingLevel; level >= 0; level--) {
            HashMap<String, SymbolTableEntry> symbolTableEntryHashMap = this.symbolTable.get(level);
            SymbolTableEntry symbolTableEntry = symbolTableEntryHashMap.get(id);
            if (symbolTableEntry != null) {
                return symbolTableEntry;
            }
        }

        return null;
    }

    public void exitScope() {
        symbolTable.remove(this.nestingLevel);
        this.nestingLevel--;

        // Get last offset (before opening scope)
        if (nestingLevel >= 0) {

            HashMap<String, SymbolTableEntry> STatNL = symbolTable.get(nestingLevel);

            int maxOffset = 0;

            for (SymbolTableEntry symbolTableEntry : STatNL.values()) {
                int symbolTableEntryOffset = symbolTableEntry.getOffset();
                if (symbolTableEntryOffset > maxOffset) {
                    maxOffset = symbolTableEntryOffset;
                }
            }

            this.offset = maxOffset;
        }
    }

    public SymbolTable replace(SymbolTable st) {
        nestingLevel = st.getNestingLevel();
        offset = st.getOffset();
        symbolTable = st.getSymbolTable();
        return this;
    }

    public static SymbolTable max(final SymbolTable env1, final SymbolTable env2) {
        return operateOnEnvironments(env1, env2, Effect::max);
    }

    public static SymbolTable seq(final SymbolTable env1, final SymbolTable env2) {
        return operateOnEnvironments(env1, env2, Effect::seq);
    }

    public SymbolTable setNL(Integer nestingLevel) {
        this.nestingLevel = nestingLevel;
        return this;
    }

    public SymbolTable setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    private static SymbolTable operateOnEnvironments(final SymbolTable env1, final SymbolTable env2,
                                                     final BiFunction<Integer, Integer, Integer> operation) {
        SymbolTable resultEnv = new SymbolTable().setNL(env1.getNestingLevel()).setOffset(env1.getOffset());
        for (int i = 0, size = env1.symbolTable.size(); i < size; i++) { // for each scope in the Symbol Table
            HashMap<String, SymbolTableEntry> ithScope1 = env1.symbolTable.get(i);
            HashMap<String, SymbolTableEntry> ithScope2 = env2.symbolTable.get(i);
            final HashMap<String, SymbolTableEntry> resultEnvScope = new HashMap<>();
            for (String id : ithScope1.keySet()) {
                SymbolTableEntry entryInScopeEnv1 = ithScope1.get(id);
                SymbolTableEntry entryInScopeEnv2 = ithScope2.get(id);

                if (entryInScopeEnv2 != null) { // ==> id \in dom(env2)
                    SymbolTableEntry opEntry = new SymbolTableEntry(entryInScopeEnv1.getNestinglevel(), entryInScopeEnv1.getType(), entryInScopeEnv1.getOffset());

                    opEntry.getStatus().newStatus(1, operation.apply(entryInScopeEnv1.getStatus().getActualStatus(1), entryInScopeEnv2.getStatus().getActualStatus(1)));

                    resultEnvScope.put(id, opEntry);
                } else {
                    resultEnvScope.put(id, entryInScopeEnv1);
                }
            }
            resultEnv.symbolTable.add(resultEnvScope);
        }
        return resultEnv;
    }

    public ArrayList<CustomError> checkForNotInitializedOrNotUsedVariables() {
        ArrayList<CustomError> customErrors = new ArrayList<>();

        HashMap<String, SymbolTableEntry> symbolTableEntryHashMap = getSymbolTableAtCurrentLevel();

        for (String key : symbolTableEntryHashMap.keySet()) {
            SymbolTableEntry symbolTableEntry = symbolTableEntryHashMap.get(key);

            //for(Integer i = 0; i < symbolTableEntry.getStatus().getMaxRefLevel(); i++) {

            Integer status = symbolTableEntry.getStatus().getActualStatus(symbolTableEntry.getStatus().getMaxRefLevel());
            Boolean isUsed = symbolTableEntry.getStatus().getUsed(symbolTableEntry.getStatus().getMaxRefLevel());

            if (status.equals(Effect.bot)) {
                customErrors.add(new CustomError("Variable " + key + " not used", true));
            } else {
                if (!isUsed) {
                    customErrors.add(new CustomError("Id " + key + " initialized but not used", true));
                }
            }
            //}


        }

        return customErrors;
    }

    public static SymbolTable par(final SymbolTable env1, final SymbolTable env2) {
        SymbolTable resultingEnvironment = new SymbolTable();
        resultingEnvironment.newScope();

        Map<String, SymbolTableEntry> scope1 = env1.getSymbolTable().get(env1.getSymbolTable().size() - 1);
        Map<String, SymbolTableEntry> scope2 = env2.getSymbolTable().get(env2.getSymbolTable().size() - 1);

        for (var xInE1 : scope1.entrySet()) {
            if (!scope2.containsKey(xInE1.getKey())) {

                HashMap<String, SymbolTableEntry> STAtCurrentNL = resultingEnvironment.getSymbolTableAtCurrentLevel();

                SymbolTableEntry entry = new SymbolTableEntry(xInE1.getValue().getNestinglevel(), xInE1.getValue().getType(), resultingEnvironment.getOffset());

                STAtCurrentNL.put(xInE1.getKey(), entry);
                for (int j = 0; j < xInE1.getValue().getStatus().getMaxRefLevel(); j++) {
                    entry.getStatus().newStatus(xInE1.getValue().getStatus().getActualStatus(0), j);
                }
            }
        }

        for (var xInE2 : scope2.entrySet()) {
            if (!scope1.containsKey(xInE2.getKey())) {
                HashMap<String, SymbolTableEntry> STAtCurrentNL = resultingEnvironment.getSymbolTableAtCurrentLevel();

                SymbolTableEntry entry = new SymbolTableEntry(xInE2.getValue().getNestinglevel(), xInE2.getValue().getType(), resultingEnvironment.getOffset());

                STAtCurrentNL.put(xInE2.getKey(), entry);
                for (int j = 0; j < xInE2.getValue().getStatus().getMaxRefLevel(); j++) {
                    entry.getStatus().newStatus(xInE2.getValue().getStatus().getActualStatus(0), j);
                }
            }
        }

        for (var xInE1 : scope1.entrySet()) {
            for (var xInE2 : scope2.entrySet()) {
                if (xInE1.getKey().equals(xInE2.getKey())) {
                    HashMap<String, SymbolTableEntry> STAtCurrentNL = resultingEnvironment.getSymbolTableAtCurrentLevel();

                    SymbolTableEntry entry = new SymbolTableEntry(xInE2.getValue().getNestinglevel(), xInE2.getValue().getType(), resultingEnvironment.getOffset());

                    STAtCurrentNL.put(xInE2.getKey(), entry);
                    for (int j = 0; j < xInE2.getValue().getStatus().getMaxRefLevel(); j++) {
                        entry.getStatus().newStatus(Effect.par(xInE1.getValue().getStatus().getActualStatus(0), xInE2.getValue().getStatus().getActualStatus(0)), j);
                    }
                }
            }
        }


        return resultingEnvironment;

    }

    public static SymbolTable update(SymbolTable env1, SymbolTable env2) {
        SymbolTable returnedEnvironment;

        if (env2.symbolTable.size() == 0 || env1.symbolTable.size() == 0) {
            return new SymbolTable().replace(env1);
        }

        Map<String, SymbolTableEntry> headScope1 = env1.symbolTable.get(env1.symbolTable.size() - 1);
        Map<String, SymbolTableEntry> headScope2 = env2.symbolTable.get(env2.symbolTable.size() - 1);

        if (headScope2.keySet().isEmpty()) {
            // \sigma' = \emptySet
            return new SymbolTable().replace(env1);
        }

        var u = headScope2.entrySet().stream().findFirst().get();

        if (headScope1.containsKey(u.getKey())) {
            headScope1.put(u.getKey(), u.getValue());

            returnedEnvironment = update(env1, env2);
        } else {
            SymbolTable envWithOnlyU = new SymbolTable();
            envWithOnlyU.newScope();
            HashMap<String, SymbolTableEntry> STAtCurrentNL = envWithOnlyU.getSymbolTableAtCurrentLevel();

            SymbolTableEntry entry = new SymbolTableEntry(u.getValue().getNestinglevel(), u.getValue().getType(), envWithOnlyU.getOffset());

            STAtCurrentNL.put(u.getKey(), entry);

            for (int j = 0; j < u.getValue().getStatus().getMaxRefLevel(); j++) {
                entry.getStatus().newStatus(u.getValue().getStatus().getActualStatus(j), j);
            }

            env1.exitScope();
            SymbolTable tmpEnv = update(env1, envWithOnlyU);

            returnedEnvironment = update(tmpEnv, env2);
        }

        return returnedEnvironment;
    }


}
