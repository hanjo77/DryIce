

import java.util.HashMap;
import java.util.Map;

import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import ch.quantasy.tinkerforge.tinker.core.implementation.TinkerforgeDevice;

import com.tinkerforge.BrickletMoisture;
import com.tinkerforge.BrickletMoisture.MoistureListener;
import com.tinkerforge.BrickletMotionDetector.DetectionCycleEndedListener;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class MoistureDetectorApplication extends AbstractTinkerforgeApplication {
	private final Map<Device, MoistureListener> listenerMap;

	public MoistureDetectorApplication() {
		this.listenerMap = new HashMap<Device, MoistureListener>();
	}

	@Override
	public void deviceDisconnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		if (device instanceof BrickletMoisture) {
			this.moistureDetectorDisconnect((BrickletMoisture) device);
		}
	}

	@Override
	public void deviceConnected(final TinkerforgeStackAgent satckAgent,
			final Device device) {
		if (device instanceof BrickletMoisture) {
			this.moistureDetectionConnect((BrickletMoisture) device);
		}
	}

	private void moistureDetectorDisconnect(
			final BrickletMoisture moistureDetector) {
		final MoistureListener listener = this.listenerMap
				.get(moistureDetector);
		if (listener != null) {
			moistureDetector.removeMoistureListener(listener);

			this.listenerMap.remove(moistureDetector);
		}

	}

	private void moistureDetectionConnect(
			final BrickletMoisture moistureDetector) {
		if (!this.listenerMap.containsKey(moistureDetector)) {
			final MoistureListener listener = new DryIceMoistureListener(
					moistureDetector);

			moistureDetector.addMoistureListener(listener);
			this.listenerMap.put(moistureDetector, listener);
		}
	}

	class DryIceMoistureListener implements MoistureListener {
		private final BrickletMoisture device;

		public DryIceMoistureListener(final BrickletMoisture device) {
			this.device = device;
			try {
				this.device.setMoistureCallbackPeriod(1000);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public BrickletMoisture getDevice() {
			return this.device;
		}

		@Override
		public void moisture(int arg0) {
			/* System.out.println(TinkerforgeDevice.toString(this.device)
					+ ":  Moisture detection"); */
			try {
				DryIceWriter writer = DryIceWriter.getInstance();
				writer.setMoisture(this.device.getMoistureValue());
				writer.printValues();
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
