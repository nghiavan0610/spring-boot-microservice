package com.benisme.inventoryservice.seeder;

import com.benisme.inventoryservice.model.Inventory;
import com.benisme.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventorySeeder implements CommandLineRunner {

    private  final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode("iphone_13");
        inventory.setQuantity(1000);

        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("iphone_13_pro");
        inventory1.setQuantity(0);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory1);
    }
}
