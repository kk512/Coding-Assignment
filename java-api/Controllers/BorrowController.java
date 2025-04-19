import com.example.library.model.*;
import com.example.library.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    @PostMapping("/{userId}/{inventoryId}")
    public BorrowRecord borrowBook(@PathVariable Long userId, @PathVariable Long inventoryId) {
        User user = userRepository.findById(userId).orElseThrow();
        BookInventory inventory = bookInventoryRepository.findById(inventoryId).orElseThrow();

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setInventory(inventory);
        record.setIssueDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusWeeks(2));

        return borrowRecordRepository.save(record);
    }
}