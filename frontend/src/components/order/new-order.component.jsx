import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import productService from "../../services/product.service";
import orderService from "../../services/order.service";
import authService from "../../services/auth.service";
import categoryService from "../../services/category.service";

export default class NewOrder extends Component {
  constructor(props) {
    super(props);
    this.handlePlaceOrder = this.handlePlaceOrder.bind(this);
    this.onChangeNote = this.onChangeNote.bind(this);
    this.onChangeCategory = this.onChangeCategory.bind(this);
    this.addProduct = this.addProduct.bind(this);
    this.deleteProduct = this.deleteProduct.bind(this);
    this.onChangePaid = this.onChangePaid.bind(this);

    this.state = {
      user: authService.getCurrentUser(),
      productIds: [],
      quantities: [],
      note: "",
      isPaid: false,
      successful: false,
      message: "",
      listCategories: [],
      listProduct: [],
      listSearch: [],
      showListProduct: true
    };
  }

  componentDidMount() {
    productService.getAllProducts().then(
      response => {
        this.setState({
          listSearch: response.data,
          content: "New Order"
        });
      },
      error => {
        this.setState({
          content:
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString()
        });
      }
    );
    categoryService.getListParent(-1).then(
      response => {
        this.setState({
          listCategories: response.data
        });
      },
      error => {}
    );
  }

  onChangeNote(e) {
    this.setState({
      note: e.target.value
    });
  }

  addProduct(product){
    if (!this.state.listProduct.find(item => item.id === product.id)){
      this.state.listProduct.push(product)
      this.setState({showListProduct: true})
    }
  }

  deleteProduct(id){
    let list = this.state.listProduct.filter(product => product.id !== id);
    this.setState({listProduct: list});

    let index = this.state.productIds.indexOf(id.toString());

    this.state.productIds.splice(index, 1);
    this.state.quantities.splice(index, 1);
  }

  onChangeCategory = event => {
    let category_id = event.target.value;
    
    productService.getAllProductsByCategory(category_id).then(
      response => {
        this.setState({
          listSearch: response.data
        });
      });

    
  }

  onChangePaid(){
    this.setState({
      isPaid: !this.state.isPaid
    });
  }

  onChangeQuantity = event => {

    let quantities = [...this.state.quantities, event.target.value];
    let productIds = [...this.state.productIds, event.target.id];

      if (this.state.productIds.includes(event.target.id)){
        let id = this.state.productIds.indexOf(event.target.id);
        quantities.splice(id, 1);
        productIds.splice(id,1);
      }
      this.setState({
        quantities: quantities,
        productIds: productIds
      });
  }  

  handlePlaceOrder(e){
    e.preventDefault();
    this.setState({
      message: "",
      successful: false
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
        let orderRequest = {userId: this.state.user.id,
                            note: this.state.note,
                            productIds: this.state.productIds,
                            quantities: this.state.quantities,
                            isPaid: this.state.isPaid}

        var result = window.confirm("Do you want to place this order?");
        if (result) {
          orderService.placeOrder(orderRequest).then(
            response => {
              this.setState({
                message: response.data.message,
                successful: true
              });
            },
            error => {
              const resMessage =
                (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
                error.message ||
                error.toString();

              this.setState({
                successful: false,
                message: resMessage
              });
            }
          );
        }
    }
  }

  render() {
    return (
      <div className="jumbotron" >
        {!this.state.user ? (
        <div style={{marginTop: "50px"}}>
          <h3>Please Login</h3>
        </div>
        ):(
        <div className="card col-8" style={{marginTop: "50px"}}>
          <h3>New order</h3>
          <Form
            onSubmit={this.handlePlaceOrder}
            ref={c => {
              this.form = c;
            }}
            >
            {!this.state.successful && (
              <div>
                <div className="row form-group">
                  <label htmlFor="note" className="col-sm-1">Note</label>
                  <div className="col-sm">
                    <Input
                      type="text"
                      className="form-control"
                      name="note"
                      value={this.state.note}
                      onChange={this.onChangeNote}
                    />
                  </div>
                  <label htmlFor="isPaid" className="col-sm-1">Paid</label>
                  <div className="col-sm-1">
                    <Input
                    type="checkbox"
                    className="form-control"
                    name="isPaid"
                    value={this.state.isPaid}
                    onChange={this.onChangePaid}
                    />
                  </div>
                  
                </div>
              
                <div className="row form-group">

                  <div className="col-sm-4">
                      <div className="form-group">
                        <label htmlFor="parentId">Category</label>
                        <select
                          className="form-control"
                          name="category"
                          onChange={this.onChangeCategory}
                        >
                        {
                          this.state.listCategories.map(item =>{
                            return (
                            <option key={item.id} value={item.id}>{item.name}</option>
                            )
                          })
                        }
                        </select>
                      </div>
                    
                    { 
                      <table className = "table table-striped table-bordered">
                            <tbody>
                                {
                                    this.state.listSearch.map(product =>                     
                                      <tr key = {product.id}>
                                             <td>
                                               <img alt="pic" height="50px" width="50px" src={
                                                 product.pictureUri === null ? 
                                                 "https://trentini.com.br/wp-content/uploads/2016/04/dummy-post-square-1-768x768.jpg" : product.pictureUri
                                               }/>
                                             </td>
                                             <td> {product.name} </td>                                    
                                             <td>
                                                <button type="button" onClick={ () => this.addProduct(product)} className="btn btn-primary">Add </button>                                                     
                                             </td>
                                      </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    }
                  </div>
                  
                  <div className="col-sm">
                    <label htmlFor="quantities">Products</label>
                    <table className = "table table-striped table-bordered">
                      <thead>
                        <tr>
                          <th>Name</th>
                          <th>Quantity</th>
                          <th>Price</th>
                          <th>Action</th>
                        </tr>
                      </thead>
                      {this.state.showListProduct &&
                      <tbody>
                        {this.state.listProduct.map(product => 
                        <tr key = {product.id}>
                          <td>{product.name}</td>
                          <td>
                            <Input
                              type="number"
                              className="form-control"
                              name="quantity"
                              id = {product.id}
                              onChange={this.onChangeQuantity}
                            />
                            In stock: {product.quantity}
                          </td>
                          <td>{product.inpPrice} $ / {product.unit}</td>
                          <td>
                            <button onClick={ () => this.deleteProduct(product.id)} className="btn btn-danger">Delete </button>    
                          </td>
                        </tr>
                        )}
                      </tbody>
                      }
                    </table>
                  </div>
                

                </div>
              
                <div className="row form-group">
                  
                </div>

                <div className="row justify-content-center form-group">
                  <button className="btn btn-primary btn-block col-sm-2 justify-content-center">Place Order</button>
                </div>
              </div>
            )}

            {this.state.message && (
              <div className="form-group">
                <div
                  className={
                    this.state.successful
                      ? "alert alert-success"
                      : "alert alert-danger"
                  }
                  role="alert"
                >
                  {this.state.message}
                </div>
              </div>
            )}
            <CheckButton
              style={{ display: "none" }}
              ref={c => {
                this.checkBtn = c;
              }}
            />
          </Form>
        </div>
        )}
      </div>
    );
  }
}
