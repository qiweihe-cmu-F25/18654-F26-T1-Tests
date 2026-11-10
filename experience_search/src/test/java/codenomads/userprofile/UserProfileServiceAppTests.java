// package codenomads.userprofile;

// import org.jboss.arquillian.junit.Arquillian;
// import org.junit.Assert;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.jboss.arquillian.test.api.ArquillianResource;
// import org.junit.runner.RunWith;


// import org.jboss.arquillian.container.test.api.Deployment;
// import org.jboss.shrinkwrap.api.ShrinkWrap;
// import org.jboss.shrinkwrap.api.spec.WebArchive;

// import java.net.URL;

// //@SpringBootTest
// @RunWith(Arquillian.class)
// public class UserProfileServiceAppTests {

// 	@ArquillianResource
// 	private URL deploymentUrl;

// 	@Deployment
// 	public static WebArchive createDeployment() {
// 		return ShrinkWrap.create(WebArchive.class)
// 				.addClass(UserProfileServiceApp.class)
// 				// Add other classes and resources needed for the test
// 				.addAsManifestResource("META-INF/beans.xml", "beans.xml");
// 	}

// 	@Test
// 	public void testDeployment() {
// 		Assert.assertNotNull(deploymentUrl);
// 		// Additional test logic
// 	}

// }
