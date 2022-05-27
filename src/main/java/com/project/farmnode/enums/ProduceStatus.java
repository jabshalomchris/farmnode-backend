package com.project.farmnode.enums;

public enum ProduceStatus {
    GROWING{
        @Override
        public ProduceStatus nextState() {
            return RIPE;
        }
        @Override
        public String responsibleState() {
            return "GROWING";
        }
    },
    RIPE{
        @Override
        public ProduceStatus nextState() {
            return GROWING;
        }
        @Override
        public String responsibleState() {
            return "RIPE";
        }
    };
    public abstract ProduceStatus nextState();
    public abstract String responsibleState();
}
