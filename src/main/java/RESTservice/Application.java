package RESTservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class Application implements CommandLineRunner
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception
    {
        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE records IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE records(key VARCHAR(255), value VARCHAR(255))");
        log.info("Tables created");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Record> getAll()
    {
        log.info("Get: *");
        return jdbcTemplate.query(
                "SELECT key, value FROM records", new Object[]{},
                (rs, rowNum) -> new Record(rs.getString("key"), rs.getString("value")));
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public List<Record> getByKey(@PathVariable String key)
    {
        log.info(String.format("Get: %s", key));
        return jdbcTemplate.query(
                "SELECT key, value FROM records WHERE key = ?", new Object[]{key},
                (rs, rowNum) -> new Record(rs.getString("key"), rs.getString("value")));
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public List<Record> putByJson(@RequestBody Record record)
    {
        jdbcTemplate.update("INSERT INTO records(key, value) VALUES (?,?)", record.name, record.phone);
        log.info(String.format("Put: %s", record.toString()));
        return getAll();
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.DELETE)
    public List<Record> deleteByKey(@PathVariable String key)
    {
        jdbcTemplate.update("DELETE FROM records WHERE name = ?", key);
        log.info(String.format("Deleted: %s", key));
        return getAll();
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public List<Record> deleteAll()
    {
        jdbcTemplate.update("DELETE FROM records");
        log.info("Deleted: *");
        return getAll();
    }
}