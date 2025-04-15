package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.UUID;


@Getter
@Setter
@ToString
public class Url {
    private UUID id;
    private String name;
    private Timestamp createdAt;
}
