import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;



public class DryIceApplication extends AbstractTinkerforgeApplication {
	private final MasterBrickApplication masterBrickApplication;
	private final MoistureDetectorApplication moistureDetectorApplication;
	private final TemperatureDetectorApplication temperatureDetectorApplication;

	public DryIceApplication() {
		this.masterBrickApplication = new MasterBrickApplication();
		this.moistureDetectorApplication = new MoistureDetectorApplication();
		this.temperatureDetectorApplication = new TemperatureDetectorApplication();
		super.addTinkerforgeApplication(this.masterBrickApplication, 
				this.temperatureDetectorApplication,
				this.moistureDetectorApplication);
	}

	@Override
	public void deviceDisconnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceConnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		if (device instanceof BrickMaster) {
			final BrickMaster masterDevice = (BrickMaster) device;
			try {
				if (masterDevice.isWifiPresent()) {
					System.out.println("The secret key is: "
							+ masterDevice.getWifiEncryption().key);
					masterDevice
							.setWifiPowerMode(BrickMaster.WIFI_POWER_MODE_LOW_POWER);
				}
			} catch (final TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final NotConnectedException e) {
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
