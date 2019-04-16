package activiti.db.web.model.vo;

import java.util.Map;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-16 06:43
 */
public class TaskVo {
    private String id;
    private String name;
    private String comment;
    private Map<String, Object> variables;




    public TaskVo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    //TODO effectiveJava 换成Build ??
    public TaskVo(String id, String name, String comment) {
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
