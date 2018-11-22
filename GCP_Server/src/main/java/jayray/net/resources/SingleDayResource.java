package jayray.net.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import jayray.net.dao.UserDao;
import jayray.net.model.User;

@Path("/single")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class SingleDayResource {
	@GET
	@Path("{userID}/{day}")
	public String getFeatures(
			@PathParam("userID") int userID,
			@PathParam("day") int day) throws SQLException {		
		return UserDao.getSingleDay(userID, day);
	}
}
