package com.cloud.shopping.service;

import com.cloud.shopping.client.CustomerClient;
import com.cloud.shopping.client.ProductCliente;
import com.cloud.shopping.entity.Invoice;
import com.cloud.shopping.entity.InvoiceItem;
import com.cloud.shopping.model.Customer;
import com.cloud.shopping.model.Product;
import com.cloud.shopping.repository.InvoiceItemsRepository;
import com.cloud.shopping.repository.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemsRepository invoiceItemsRepository;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    ProductCliente productCliente;

    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }


    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoiceDB != null) {
            return invoiceDB;
        }
        invoice.setState("CREATED");
        invoiceDB = invoiceRepository.save(invoice);
        invoiceDB.getItems().forEach(invoiceItem -> {
            double quantity = invoiceItem.getQuantity() * -1;
            productCliente.updateStockProduct(invoiceItem.getProductId(), quantity);
        });

        return invoiceDB;
    }


    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null) {
            return null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepository.save(invoiceDB);
    }


    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null) {
            return null;
        }
        invoiceDB.setState("DELETED");
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice getInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice != null) {
            Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
            invoice.setCustomer(customer);

/*
            List<InvoiceItem> listItems = invoice.getItems().stream().map(invoiceItem -> {
                Product product = productCliente.getProduct(invoiceItem.getProductId()).getBody();
                invoiceItem.setProduct(product);
                return invoiceItem;
            }).collect(Collectors.toList());
            invoice.setItems(listItems);
*/

            List<InvoiceItem> listNewItems = new ArrayList<>();
            invoice.getItems().forEach(invoiceItem -> {
                Product product = productCliente.getProduct(invoiceItem.getProductId()).getBody();
                invoiceItem.setProduct(product);
                listNewItems.add(invoiceItem);
            });
            System.out.printf("****************");
            listNewItems.forEach(System.out::println);
            System.out.println("tamanio "+listNewItems.size());
            invoice.setItems(listNewItems);
        }
        return invoice;
    }
}
