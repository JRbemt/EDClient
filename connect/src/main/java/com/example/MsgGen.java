package com.example;

import static java.lang.Math.round;

public class MsgGen {
    private MsgGen(){
        throw new IllegalAccessError("This class can't be instantiated");
    }

    // Door message codes
    private final static int LEFT_DOOR_IN = 1;
    private final static int RIGHT_DOOR_IN = 2;
    private final static int LEFT_DOOR_OUT = 3;
    private final static int RIGHT_DOOR_OUT = 4;
    private final static int LEFT_DOOR_STOP = 5;
    private final static int RIGHT_DOOR_STOP = 6;
    private final static int BOTH_DOORS_IN = 7;
    private final static int BOTH_DOORS_OUT = 8;
    private final static int BOTH_DOORS_STOP = 9;

    // Slope message codes
    private final static int SLOPE_STOP = 0;
    private final static int SLOPE_UP = 1;
    private final static int SLOPE_DOWN = 2;

    // Body codes
    private final static String DOOR_BODY = "d";
    private final static String SLOPE_BODY = "s";
    private final static String WHEELS_BODY = "w";
    private final static String LED_BODY = "l";
    private final static String GIMBAL_BODY = "g";

    // "API" codes
    // Doors
    public static final int D_LEFT = 0;
    public static final int D_RIGHT = 1;
    public static final int D_BOTH = 2;

    public static final int D_IN = 3;
    public static final int D_OUT = 4;
    public static final int D_STOP = 5;

    // Slope
    public static final int S_UP = 0;
    public static final int S_DOWN = 1;
    public static final int S_STOP = 2;

    // Gimbal
    public static final int G_STOP = 0;
    public static final int G_LEFT = 2;
    public static final int G_RIGHT = 3;
    public static final int G_UP = 4;
    public static final int G_DOWN = 5;


    private static String createMsg(String body, int code, int speed) {
        return "#" + body + "@" + code + "@" + speed + "!";
    }

    // "API" Functions
    public static String door(int side, int direction) {
        int instruction = -1;

        switch (side) {
            case D_LEFT:
                switch (direction) {
                    case D_IN:
                        instruction = LEFT_DOOR_IN;
                        break;
                    case D_OUT:
                        instruction = LEFT_DOOR_OUT;
                        break;
                    case D_STOP:
                        instruction = LEFT_DOOR_STOP;
                        break;
                }
                break;
            case D_RIGHT:
                switch (direction) {
                    case D_IN:
                        instruction = RIGHT_DOOR_IN;
                        break;
                    case D_OUT:
                        instruction = RIGHT_DOOR_OUT;
                        break;
                    case D_STOP:
                        instruction = RIGHT_DOOR_STOP;
                        break;
                }
                break;
            case D_BOTH:
                switch (direction) {
                    case D_IN:
                        instruction = BOTH_DOORS_IN;
                        break;
                    case D_OUT:
                        instruction = BOTH_DOORS_OUT;
                        break;
                    case D_STOP:
                        instruction = BOTH_DOORS_STOP;
                        break;
                }
                break;
        }
        return createMsg(DOOR_BODY, instruction, 0);
    }

    public static String slope(int direction) {
        int instruction = -1;

        switch(direction) {
            case S_UP:
                instruction = SLOPE_UP;
                break;
            case S_DOWN:
                instruction = SLOPE_DOWN;
                break;
            case S_STOP:
                instruction = SLOPE_STOP;
                break;
        }
        return createMsg(SLOPE_BODY, instruction, 0);
    }

    public static String wheels(double verticalSpeed, double horizontalSpeed) {
        return createMsg(WHEELS_BODY, 50 + (int) round(50.0*verticalSpeed), 50 + (int) round(50.0*horizontalSpeed));
    }

    public static String led(boolean status) {
        return createMsg(LED_BODY, status ? 1 : 0, 0);
    }

    public static String gimbal(int state) {
        return createMsg(GIMBAL_BODY, state, 0);
    }
}