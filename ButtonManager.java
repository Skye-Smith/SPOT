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

    private GpioPinDigitalInput aButton;
    private boolean aState;

    
    private GpioPinDigitalInput bButton;
    private boolean bState;

    /*
    private GpioPinDigitalInput soundGate;
    private boolean soundState;
    */

    public ButtonManager() {
        aState = false;
        
        bState = false;
        /*
        soundState = false;
        */

        gpio = GpioFactory.getInstance();

        // Alter to accomodate wiring of hb sensor -------------------------------------
        GpioPinDigitalInput aButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
        
        GpioPinDigitalInput bButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        /*
        GpioPinDigitalInput soundGate = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
        */

        aButton.setShutdownOptions(true);
        
        bButton.setShutdownOptions(true);
        /*
        soundState.setShutdownOptions(true);
        */

        aButton.addListener(new AListener());
        
        bButton.addListener(new BListener());
        /*
        soundGate.addListener(new SoundListener());
        */
    }

    public boolean getA() {
        boolean temp = aState;
        aState = false;
        return temp;
    }

    
    public boolean getB() {
        boolean temp = bState;
        bState = false;
        return temp;
    }

    /*
    public boolean getSound() {
        boolean temp = soundState;
        soundState = false;
        return temp;
    }
    */

    private class AListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            if ((e.getState().toString()).equals("HIGH")) {
                System.out.println("A High!");
                aState = true;
            }
        }
    }

    private class BListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            if (e.getState().toString().equals("HIGH")) {
                System.out.println("B High!");
                bState = true;
            }
        }
    }

    /*
    private class SoundListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
            System.out.println("StateChange!");
            if (e.getState().equals("HIGH")) {
                soundState = true;
            }
        }
    }
    */
}