import java.util.HashMap;
import java.util.Map;

import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import ch.quantasy.tinkerforge.tinker.core.implementation.TinkerforgeDevice;

import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTemperatureIR.ObjectTemperatureListener;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class TemperatureDetectorApplication extends AbstractTinkerforgeApplication {
	private final Map<Device, ObjectTemperatureListener> listenerMap;

	public TemperatureDetectorApplication() {
		this.listenerMap = new HashMap<Device, ObjectTemperatureListener>();
	}

	@Override
	public void deviceDisconnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		if (device instanceof BrickletTemperatureIR) {
			this.temperatureDetectorDisconnect((BrickletTemperatureIR) device);
		}
	}

	@Override
	public void deviceConnected(final TinkerforgeStackAgent satckAgent,
			final Device device) {
		if (device instanceof BrickletTemperatureIR) {
			this.temperatureDetectionConnect((BrickletTemperatureIR) device);
		}
	}

	private void temperatureDetectorDisconnect(
			final BrickletTemperatureIR moistureDetector) {
		final ObjectTemperatureListener listener = this.listenerMap
				.get(moistureDetector);
		if (listener != null) {
			moistureDetector.removeObjectTemperatureListener(listener);

			this.listenerMap.remove(moistureDetector);
		}

	}

	private void temperatureDetectionConnect(
			final BrickletTemperatureIR moistureDetector) {
		if (!this.listenerMap.containsKey(moistureDetector)) {
			final ObjectTemperatureListener listener = new DryIceTemperatureListener(
					moistureDetector);

			moistureDetector.addObjectTemperatureListener(listener);
			this.listenerMap.put(moistureDetector, listener);
		}
	}

	class DryIceTemperatureListener implements ObjectTemperatureListener {
		private final BrickletTemperatureIR device;

		public DryIceTemperatureListener(final BrickletTemperatureIR device) {
			this.device = device;
			try {
				this.device.setObjectTemperatureCallbackPeriod(1000);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public BrickletTemperatureIR getDevice() {
			return this.device;
		}

		@Override
		public void objectTemperature(short arg0) {
			try {
				DryIceWriter.getInstance().setTemperature(this.device.getObjectTemperature()/10);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		return this.getClass() == obj.getClass();
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
