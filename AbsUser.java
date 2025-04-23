package FlowerShop.View;

import FlowerShop.Model.*;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AbsUser {

    ModelUser login(ModelLogin login) throws SQLException;
}