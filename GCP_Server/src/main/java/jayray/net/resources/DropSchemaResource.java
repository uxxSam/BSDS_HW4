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

@Path("/dropDataBase")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class DropSchemaResource {
	@GET
	public String getFeatures() throws SQLException {		
		return UserDao.dropSchema();
	}
}
