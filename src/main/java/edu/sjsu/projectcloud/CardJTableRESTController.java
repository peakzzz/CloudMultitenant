package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.jtable.JTableResult;
import edu.sjsu.projectcloud.session.UserSessionInfo;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;
import edu.sjsu.projectcloud.task.TaskWF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by preetikrishnan on 5/3/15.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("cmpe281project/api/kanban")
public class CardJTableRESTController {

    @Autowired
    UserSessionInfo userSessionInfo;
    private TaskWF tasks;

    @RequestMapping(value = "/listCards", method = RequestMethod.POST)
    public JTableResult getAllTasksForJTable(@RequestParam(value = "projectId", required = true) String projectId) {
        JTableResult<Task> result = new JTableResult<>(true);
        result.setResult("OK");

        AppHandler appHandler = new AppHandler();
        List<Task> tasks = appHandler.getAllTasks(projectId);

        for (Task t : tasks) {
            result.addRecord(t);
        }
        return result;
    }

    @RequestMapping(value="/createCard", method=RequestMethod.POST)
    public JTableResult<? extends Task> createTask(@ModelAttribute TaskWF tasks,
                                                   @RequestParam("projectId") String projectId,
                                                   Model model) {
        AppHandler appHandler = new AppHandler();
        tasks.setId(null);
        appHandler.addTaskToProject(tasks, projectId);

        JTableResult<TaskWF> result = new JTableResult<>();
        result.setResult("OK");
        result.addRecord(tasks);
        System.out.println(tasks);
        return result;
    }

    @RequestMapping(value="/updateCard", method=RequestMethod.POST)
    public JTableResult<? extends Task> updateTaskInProject(@ModelAttribute TaskWF tasks,
                                                            @RequestParam("projectId") String projectId,
                                                            Model model) {
        AppHandler appHandler = new AppHandler();
        appHandler.updateTaskInProject(tasks, projectId);

        JTableResult<TaskWF> result = new JTableResult<>();
        result.setResult("OK");
        result.addRecord(tasks);
        System.out.println(tasks);
        return result;
    }

    @RequestMapping(value="/deleteCard", method=RequestMethod.POST)
    public JTableResult<? extends Task> deleteTaskFromProject(@ModelAttribute TaskWF tasks,
                                                              @RequestParam("projectId") String projectId,
                                                              Model model) {
        AppHandler appHandler = new AppHandler();
        appHandler.deleteTaskFromProject(projectId, tasks.getId());
        return new JTableResult<>();
    }
}
