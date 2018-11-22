package jayray.net.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import jayray.net.dao.UserDao;
import jayray.net.model.User;

@Path("/")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class StepCountResource {
	@POST
	@Path("{userID}/{day}/{timeInterval}/{stepCount}")
	public String getFeatures(
			@PathParam("userID") int userID,
			@PathParam("day") int day,
			@PathParam("timeInterval") int timeInterval,
			@PathParam("stepCount") int stepCount) throws SQLException {		
		return UserDao.postStepCount(userID, day, timeInterval, stepCount);
	}
}
