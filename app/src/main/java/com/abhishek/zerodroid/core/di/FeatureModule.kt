package com.abhishek.zerodroid.core.di

import android.bluetooth.BluetoothManager
import android.content.Context
import android.hardware.ConsumerIrManager
import android.hardware.usb.UsbManager
import android.net.wifi.WifiManager
import android.net.wifi.aware.WifiAwareManager
import android.telephony.TelephonyManager
import com.abhishek.zerodroid.core.database.AppDatabase
import com.abhishek.zerodroid.features.ble.data.BleRepository
import com.abhishek.zerodroid.features.ble.domain.BleScanner
import com.abhishek.zerodroid.features.ble.domain.GattExplorer
import com.abhishek.zerodroid.features.bluetooth_classic.domain.BluetoothClassicScanner
import com.abhishek.zerodroid.features.bluetooth_classic.domain.SdpServiceDiscovery
import com.abhishek.zerodroid.features.bluetooth_classic.domain.SppConnectionManager
import com.abhishek.zerodroid.features.camera.data.QrRepository
import com.abhishek.zerodroid.features.celltower.domain.CellTowerAnalyzer
import com.abhishek.zerodroid.features.gps.domain.GpsTracker
import com.abhishek.zerodroid.features.gps_spoof_detector.domain.GpsSpoofDetector
import com.abhishek.zerodroid.features.hidden_camera.domain.HiddenCameraDetector
import com.abhishek.zerodroid.features.ir.domain.IrTransmitter
import com.abhishek.zerodroid.features.network_scanner.domain.NetworkVulnerabilityScanner
import com.abhishek.zerodroid.features.nfc.data.NfcRepository
import com.abhishek.zerodroid.features.nfc.domain.MifareClassicReader
import com.abhishek.zerodroid.features.nfc.domain.NfcTagManager
import com.abhishek.zerodroid.features.sdr.domain.SdrDetector
import com.abhishek.zerodroid.features.sensors.domain.SensorDataCollector
import com.abhishek.zerodroid.features.ultrasonic.domain.UltrasonicAnalyzer
import com.abhishek.zerodroid.features.usb.domain.UsbDeviceManager
import com.abhishek.zerodroid.features.usbcamera.domain.UsbCameraDetector
import com.abhishek.zerodroid.features.uwb.domain.UwbService
import com.abhishek.zerodroid.features.wardriving.data.WardrivingRepository
import com.abhishek.zerodroid.features.wardriving.domain.WardrivingCollector
import com.abhishek.zerodroid.features.wifi.domain.WifiScanner
import com.abhishek.zerodroid.features.wifi_direct.domain.WifiDirectFileTransfer
import com.abhishek.zerodroid.features.wifi_direct.domain.WifiDirectManager
import com.abhishek.zerodroid.features.wifiaware.domain.WifiAwareService
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Mirrors what AppContainer used to lazily construct. Providers with no scope
 * annotation are intentionally unscoped (fresh instance per injection) because
 * that's how AppContainer's ViewModel factories built them - never cached.
 */
@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {

    // Phase 1 features
    @Provides
    @Singleton
    fun provideSensorDataCollector(sensorManager: SensorManager): SensorDataCollector =
        SensorDataCollector(sensorManager)

    @Provides
    @Singleton
    fun provideWifiScanner(@ApplicationContext context: Context, wifiManager: WifiManager): WifiScanner =
        WifiScanner(context, wifiManager)

    @Provides
    @Singleton
    fun provideBleScanner(bluetoothManager: BluetoothManager?): BleScanner =
        BleScanner(bluetoothManager)

    @Provides
    @Singleton
    fun provideBleRepository(bleScanner: BleScanner, database: AppDatabase): BleRepository =
        BleRepository(bleScanner, database.bleDeviceDao())

    // Phase 2 features
    @Provides
    @Singleton
    fun provideUsbDeviceManager(@ApplicationContext context: Context, usbManager: UsbManager?): UsbDeviceManager =
        UsbDeviceManager(context, usbManager)

    @Provides
    @Singleton
    fun provideCellTowerAnalyzer(telephonyManager: TelephonyManager?): CellTowerAnalyzer =
        CellTowerAnalyzer(telephonyManager)

    @Provides
    @Singleton
    fun provideIrTransmitter(irManager: ConsumerIrManager?): IrTransmitter =
        IrTransmitter(irManager)

    @Provides
    @Singleton
    fun provideNfcTagManager(): NfcTagManager = NfcTagManager()

    @Provides
    @Singleton
    fun provideNfcRepository(nfcTagManager: NfcTagManager, database: AppDatabase): NfcRepository =
        NfcRepository(nfcTagManager, database.nfcTagDao())

    @Provides
    @Singleton
    fun provideSdrDetector(usbManager: UsbManager?): SdrDetector = SdrDetector(usbManager)

    @Provides
    @Singleton
    fun provideUltrasonicAnalyzer(): UltrasonicAnalyzer = UltrasonicAnalyzer()

    @Provides
    @Singleton
    fun provideWifiAwareService(
        @ApplicationContext context: Context,
        wifiAwareManager: WifiAwareManager?
    ): WifiAwareService = WifiAwareService(context, wifiAwareManager)

    @Provides
    @Singleton
    fun provideQrRepository(database: AppDatabase): QrRepository =
        QrRepository(database.qrScanResultDao())

    @Provides
    @Singleton
    fun provideWardrivingCollector(@ApplicationContext context: Context, wifiManager: WifiManager): WardrivingCollector =
        WardrivingCollector(context, wifiManager)

    @Provides
    @Singleton
    fun provideWardrivingRepository(collector: WardrivingCollector, database: AppDatabase): WardrivingRepository =
        WardrivingRepository(collector, database.wardrivingDao())

    @Provides
    @Singleton
    fun provideUwbService(@ApplicationContext context: Context): UwbService = UwbService(context)

    @Provides
    @Singleton
    fun provideUsbCameraDetector(@ApplicationContext context: Context, usbManager: UsbManager?): UsbCameraDetector =
        UsbCameraDetector(context, usbManager)

    @Provides
    @Singleton
    fun provideGpsTracker(@ApplicationContext context: Context): GpsTracker = GpsTracker(context)

    @Provides
    @Singleton
    fun provideWifiDirectManager(@ApplicationContext context: Context): WifiDirectManager =
        WifiDirectManager(context)

    // Phase 3 features
    @Provides
    @Singleton
    fun provideBluetoothClassicScanner(
        @ApplicationContext context: Context,
        bluetoothManager: BluetoothManager?
    ): BluetoothClassicScanner = BluetoothClassicScanner(context, bluetoothManager)

    @Provides
    @Singleton
    fun provideSppConnectionManager(bluetoothManager: BluetoothManager?): SppConnectionManager =
        SppConnectionManager(bluetoothManager)

    @Provides
    @Singleton
    fun provideMifareClassicReader(): MifareClassicReader = MifareClassicReader()

    @Provides
    @Singleton
    fun provideSdpServiceDiscovery(
        @ApplicationContext context: Context,
        bluetoothManager: BluetoothManager?
    ): SdpServiceDiscovery = SdpServiceDiscovery(context, bluetoothManager)

    @Provides
    @Singleton
    fun provideWifiDirectFileTransfer(@ApplicationContext context: Context): WifiDirectFileTransfer =
        WifiDirectFileTransfer(context)

    // Phase 4 features
    @Provides
    @Singleton
    fun provideHiddenCameraDetector(@ApplicationContext context: Context): HiddenCameraDetector =
        HiddenCameraDetector(context)

    // Previously constructed fresh inside a ViewModel Factory (never cached on
    // AppContainer) - left unscoped here so each injection gets a new instance.
    @Provides
    fun provideGpsSpoofDetector(@ApplicationContext context: Context): GpsSpoofDetector =
        GpsSpoofDetector(context)

    @Provides
    fun provideNetworkVulnerabilityScanner(
        wifiScanner: WifiScanner,
        @ApplicationContext context: Context
    ): NetworkVulnerabilityScanner = NetworkVulnerabilityScanner(wifiScanner, context)

    @Provides
    fun provideGattExplorer(
        @ApplicationContext context: Context,
        bluetoothManager: BluetoothManager?
    ): GattExplorer = GattExplorer(context, bluetoothManager)
}
