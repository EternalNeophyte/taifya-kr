package edu.psuti.alexandrov.struct.impl;

import edu.psuti.alexandrov.struct.SelfParcing;

import java.util.List;

public class Block extends SelfParcing<Statement> {

    private List<Block> innerBlocks;


    @Override
    public String mask() {
        return null;
    }

    @Override
    public String input() {
        return null;
    }

    @Override
    public Statement map(String sample) {
        return null;
    }
}
