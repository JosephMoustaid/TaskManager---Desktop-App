package Test;

import models.Board;
import models.TaskManager;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class TestTaskManager {

    public static void main(String[] args) {
        // Create a TaskManager instance
        TaskManager taskManager = new TaskManager();

        // Create a board
        Board board1 = new Board("1");
        Board board2 = new Board("2");

        // Test addBoard
        Set<Board> boards = new HashSet<>();
        taskManager.addBoard(board1, boards);
        taskManager.addBoard(board2, boards);

        if (boards.size() != 2 || !boards.contains(board1) || !boards.contains(board2)) {
            System.out.println("testAddBoard failed ❌");
        } else {
            System.out.println("testAddBoard passed ✅");
        }

        // Test saveBoard and loadBoard
        String filePath = "test_board.ser";
        taskManager.saveBoard(board1, filePath);

        Board loadedBoard = taskManager.loadBoard(filePath);
        if (loadedBoard == null || !loadedBoard.getId().equals(board1.getId())) {
            System.out.println("testSaveAndLoadBoard failed ❌");
        } else {
            System.out.println("testSaveAndLoadBoard passed ✅");
        }

        // Clean up the test file
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        // Test openAddTaskDialog (UI-related, cannot be tested in a non-UI environment)
        System.out.println("testOpenAddTaskDialog skipped (requires JavaFX environment)");
    }
}