package com.tomgs.test.design_patterns.template;

import java.util.Map;

public interface Task {

    void execute(Map<String, String> taskContext);

}
