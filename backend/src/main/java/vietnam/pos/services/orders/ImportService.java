package vietnam.pos.services.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vietnam.pos.loaders.requests.ImportRequest;
import vietnam.pos.loaders.responses.ImportItemResponse;
import vietnam.pos.loaders.responses.ImportProductResponse;
import vietnam.pos.loaders.responses.ImportResponse;
import vietnam.pos.models.orders.EOrderStatus;
import vietnam.pos.models.orders.Import;
import vietnam.pos.models.orders.ImportProduct;
import vietnam.pos.models.orders.ImportProductKey;
import vietnam.pos.models.products.Product;
import vietnam.pos.models.users.User;
import vietnam.pos.repository.orders.ImportProductRepository;
import vietnam.pos.repository.orders.ImportRepository;
import vietnam.pos.repository.products.ProductRepository;
import vietnam.pos.repository.users.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImportService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImportRepository importRepository;

    @Autowired
    ImportProductRepository importProductRepository;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<ImportItemResponse> getAllImports(){
        var responseList = new ArrayList<ImportItemResponse>();
        importRepository.findAll().forEach(imp -> {
            responseList.add(new ImportItemResponse(imp.getId(), imp.getTotal(), imp.getNote(),
                    imp.getCreatedOn(),imp.getUpdatedOn(), imp.getUser().getUsername(), imp.getStatus().toString()
            ));
        });
        return responseList;
    }

    public ImportResponse convertImportToResponse(Import imp){
        var importResponse = new ImportResponse(imp.getId(), imp.getTotal(), imp.getNote(),
                imp.getCreatedOn(), imp.getUpdatedOn(), imp.getUser().getUsername(), imp.getStatus().toString()
        );
        var listImportProducts = new ArrayList<ImportProductResponse>();
        importProductRepository.findAll().forEach(importProduct ->  {
            if(importProduct.getImp() == imp){
                listImportProducts.add(new ImportProductResponse(
                        importProduct.getProduct().getId(),
                        importProduct.getProduct().getName(),
                        importProduct.getQuantity(),
                        importProduct.getSellPrice()
                ));
            }
        });
        importResponse.setImportProducts(listImportProducts);
        return importResponse;
    }

    public void createImport(ImportRequest importRequest){
        //User
        User user = userRepository.findById(importRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Import imp = new Import(
                (float) 0,
                importRequest.isPaid()? EOrderStatus.PAID : EOrderStatus.PLACED,
                importRequest.getNote(),
                formatter.format(new Date()),
                user
        );
        importRepository.save(imp);
        //Products
        List<Long> productIds = importRequest.getProductIds();
        List<Float> quantities = importRequest.getQuantities();
        var total = (float)0;
        for (int i=0; i<productIds.size(); i++){
            Product product = productRepository.findById(productIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Error: Product not found."));

            var qty = product.getQuantity();
            product.setQuantity(qty + quantities.get(i));
            productRepository.save(product);

            ImportProductKey importProductKey = new ImportProductKey(imp.getId(),productIds.get(i));
            ImportProduct importProduct = new ImportProduct(importProductKey, imp, product, product.getInpPrice(), quantities.get(i));
            importProductRepository.save(importProduct);

            total += quantities.get(i)*product.getInpPrice();
        }
        imp.setTotal(total);
        importRepository.save(imp);
    }

    public void paidImport(Import imp){
        if(imp.getStatus() == EOrderStatus.CANCELED){
            importProductRepository.findAll().forEach(importProduct -> {
                if(importProduct.getImp() == imp){
                    Product product = importProduct.getProduct();
                    var qty = product.getQuantity();
                    product.setQuantity(qty + importProduct.getQuantity());
                    productRepository.save(product);
                }
            });
        }
        imp.setStatus(EOrderStatus.PAID);
        imp.setUpdatedOn(formatter.format(new Date()));
        importRepository.save(imp);
    }

    public void cancelImport(Import imp){
        if(imp.getStatus() != EOrderStatus.CANCELED){
            importProductRepository.findAll().forEach(importProduct -> {
                if(importProduct.getImp() == imp){
                    Product product = importProduct.getProduct();
                    var qty = product.getQuantity();
                    product.setQuantity(qty - importProduct.getQuantity());
                    productRepository.save(product);
                }
            });
            imp.setStatus(EOrderStatus.CANCELED);
            imp.setUpdatedOn(formatter.format(new Date()));
            importRepository.save(imp);
        }
    }

    public void deleteImport(Long importId){
        var importProducts = importProductRepository.findAll().stream()
                .filter(importProduct -> importProduct.getImp().getId().equals(importId))
                .collect(Collectors.toList());
        importProducts.forEach(importProduct -> {
            importProduct.setImp(null);
            importProduct.setProduct(null);
            importProductRepository.delete(importProduct);
        });
        importRepository.deleteById(importId);
    }
}
