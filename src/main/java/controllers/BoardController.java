package controllers;
import models.Board;
import utils.Database;

import java.util.Set;

public class BoardController {
    public Set<Board> getBoardData() {
        Set<Board> boards = Database.loadBoards();
        return boards ;
    }
}
