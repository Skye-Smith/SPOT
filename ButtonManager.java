import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.concurrent.Callable;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

public class ButtonManager {
    private GpioController gpio;

    private boolean aState;
    private boolean bState;
    private boolean soundState;

    public ButtonManager() {
        // Set default condition of three state variables
        aState = false;
        bState = false;
        soundState = false;

        gpio = GpioFactory.getInstance();

        // Setup/provision three pins as input, and enable the pull-down resistor
        GpioPinDigitalInput aButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput bButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput soundGate = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);

        aButton.setShutdownOptions(true);
        bButton.setShutdownOptions(true);
        soundGate.setShutdownOptions(true);

        // Add event listeners to the three provisioned pins
        aButton.addListener(new AListener());
        bButton.addListener(new BListener());
        soundGate.addListener(new SoundListener());
    }

    // Returns whether or not button A has been pressed since the last check
    public boolean getA() {
        boolean temp = aState;
        aState = false;
        return temp;
    }

    // Returns whether or not button B has been pressed since the last check
    public boolean getB() {
        boolean temp = bState;
        bState = false;
        return temp;
    }

    // Returns whether or not the sound sensor has been triggered since the last check
    public boolean getSound() {
        boolean temp = soundState;
        soundState = false;
        return temp;
    }

    // Responds to state change events, updating the value of aState to true if the new state is "HIGH"
    private class AListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            if (e.getState().toString().equals("HIGH") && !bState) {
                aState = true;
            }
        }
    }

    // Responds to state change events, updating the value of bState to true if the new state is "HIGH"
    private class BListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            if (e.getState().toString().equals("HIGH") && !aState) {
                bState = true;
            }
        }
    }

    // Responds to state change events, updating the value of soundState to true if the new state is "HIGH"
    private class SoundListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            System.out.println("StateChange!");
            if (e.getState().toString().equals("HIGH")) {
                soundState = true;
            }
        }
    }
}