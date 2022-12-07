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

        // Set the shutdown options of the three provisioned pins
        aButton.setShutdownOptions(true);
        bButton.setShutdownOptions(true);
        soundGate.setShutdownOptions(true);

        // Add event listeners to the three provisioned pins
        aButton.addListener(new AListener());
        bButton.addListener(new BListener());
        soundGate.addListener(new SoundListener());
    }

    // Returns whether or not button A has been pressed, and resets aState to false
    public boolean getA() {
        boolean temp = aState;
        aState = false;
        return temp;
    }

    // Returns whether or not button B has been pressed, and resets bState to false
    public boolean getB() {
        boolean temp = bState;
        bState = false;
        return temp;
    }

    // Returns whether or not the sound sensor has been triggered, and resets soundState to false
    public boolean getSound() {
        boolean temp = soundState;
        soundState = false;
        return temp;
    }

    // Responds to state change events, updating the value of aState to true if the new state is "HIGH"
    private class AListener implements GpioPinListenerDigital {
        // Overrides default eventhandler to instead change the state of variable aState
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            // Checks that the event is the rising edge, and that the B button has not been pressed
            if (e.getState().toString().equals("HIGH") && !bState) {
                aState = true;
            }
        }
    }

    // Responds to state change events, updating the value of bState to true if the new state is "HIGH"
    private class BListener implements GpioPinListenerDigital {
        // Overrides default eventhandler to instead change the state of variable bState
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            // Checks that the event is the rising edge, and that the A button has not been pressed
            if (e.getState().toString().equals("HIGH") && !aState) {
                bState = true;
            }
        }
    }

    // Responds to state change events, updating the value of soundState to true if the new state is "HIGH"
    private class SoundListener implements GpioPinListenerDigital {
        // Overrides default eventhandler to instead change the state of variable soundState
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            // Checks that the event is the rising edge
            if (e.getState().toString().equals("HIGH")) {
                soundState = true;
            }
        }
    }
}
