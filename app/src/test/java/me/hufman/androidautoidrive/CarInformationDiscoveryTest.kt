package me.hufman.androidautoidrive

import com.nhaarman.mockito_kotlin.*
import io.bimmergestalt.idriveconnectkit.IDriveConnection
import io.bimmergestalt.idriveconnectkit.android.CarAppResources
import io.bimmergestalt.idriveconnectkit.android.IDriveConnectionStatus
import io.bimmergestalt.idriveconnectkit.android.security.SecurityAccess
import org.junit.Test
import java.io.ByteArrayInputStream

class CarInformationDiscoveryTest {
	val iDriveConnectionStatus = mock<IDriveConnectionStatus>()
	val securityAccess = mock<SecurityAccess> {
		on { signChallenge(any(), any() )} doReturn ByteArray(512)
	}
	val carAppResources = mock<CarAppResources> {
		on { getAppCertificate() } doReturn ByteArrayInputStream(ByteArray(0))
		on { getUiDescription() } doReturn ByteArrayInputStream(ByteArray(0))
		on { getImagesDB(any()) } doReturn ByteArrayInputStream(ByteArray(0))
		on { getTextsDB(any()) } doReturn ByteArrayInputStream(ByteArray(0))
	}
	val listener = mock<CarInformationDiscoveryListener>()

	@Test
	fun testDiscovery() {
		val mockServer = MockBMWRemotingServer()
		IDriveConnection.mockRemotingServer = mockServer
		val app = CarInformationDiscovery(iDriveConnectionStatus, securityAccess, carAppResources, listener)
		app.onCreate()
		verify(listener).onCapabilities(app.capabilities!!)

		// test a CDS update
		verify(listener).onCdsConnection(any())
	}
}