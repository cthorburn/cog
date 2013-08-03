function detail(taskId) {
    
    if(taskId==0) {
        return;
    }
    
    window.parent.tbjEvents.fireEvent({ name: 'graph_task_select', data: {task: taskId}});
}

