import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import ProductService from "../../services/product.service";
import CategoryService from "../../services/category.service";

const required = value => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

const vname = value => {
  if (value.length < 3) {
    return (
      <div className="alert alert-danger" role="alert">
        The name must be longer than 3 characters.
      </div>
    );
  }
};

export default class NewProduct extends Component {
  constructor(props) {
    super(props);
    this.handleProduct = this.handleProduct.bind(this);
    this.onChangeName = this.onChangeName.bind(this);
    this.onChangeSku = this.onChangeSku.bind(this);
    this.onChangeInpPrice = this.onChangeInpPrice.bind(this);
    this.onChangeSellPrice = this.onChangeSellPrice.bind(this);
    this.onChangeQuantity = this.onChangeQuantity.bind(this);
    this.onChangeUnit = this.onChangeUnit.bind(this);
    this.onChangePicture = this.onChangePicture.bind(this);
    this.onChangeCategory = this.onChangeCategory.bind(this);

    this.state = {
      name: "",
      sku: "",
      inpPrice: "0",
      sellPrice: "0",
      quantity: "0",
      unit: "ITEM",
      pictureId: null,
      picturePreviewUrl: null,
      categoryId: 0,
      successful: false,
      message: "",
      listUnit: ["ITEM", "PIECE", "SET", "KG"],
      listCategories: []
    };
  }

  componentDidMount(){
    CategoryService.getListParent(-1).then(
      response => {
        this.setState({
          listCategories: response.data
        });
      },
      error => {}
    );
  }

  onChangeName(e) {
    this.setState({
      name: e.target.value
    });
  }

  onChangeSku(e) {
    this.setState({
      sku: e.target.value
    });
  }

  onChangeInpPrice(e){
    this.setState({
      inpPrice: e.target.value
    });
  }

  onChangeSellPrice(e){
    this.setState({
      sellPrice: e.target.value
    });
  }
  
  onChangeQuantity(e){
    this.setState({
      quantity: e.target.value
    });
  }

  onChangeUnit(e){
      this.setState({
          unit: e.target.value
      });
  }

  onChangeCategory(e)
  {
    this.setState({
      categoryId: e.target.value
    });
  }

  onChangePicture(e){
    e.preventDefault();
    const formData = new FormData();
    formData.append('file', e.target.files[0]);
    ProductService.uploadPicture(
      formData
      ).then(
      response => {
        this.setState({
          pictureId: response.data
        });
        alert("Picture uploaded successfully!");
      },
      error => {
        const resMessage =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();
        alert(resMessage);
      }
    );
    const reader = new FileReader();
    reader.readAsDataURL(e.target.files[0]);
    reader.onload = () =>{
        this.setState({
        picturePreviewUrl: reader.result
      });
    }
  }

  handleProduct(e) {
    e.preventDefault();

    this.setState({
      message: "",
      successful: false
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
      let productRequest = {
        name: this.state.name,
        sku: this.state.sku,
        inpPrice: this.state.inpPrice,
        sellPrice: this.state.sellPrice,
        quantity: this.state.quantity,
        unit: this.state.unit,
        pictureId: this.state.pictureId,
        categoryId: this.state.categoryId
      };

      ProductService.addNewProduct(
        productRequest
      ).then(
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
      this.props.history.push('/product');
    }
  }

  render() {
    return (
      <div className="col-md-12">
        <div className="card card-container">
          <Form
            onSubmit={this.handleProduct}
            ref={c => {
              this.form = c;
            }}
          >
            {!this.state.successful && (
              <div>
                <div className="form-group">
                    <label htmlFor="categoryId">Category</label>
                    <select
                      className="form-control"
                      name="categoryId"
                      value={this.state.categoryId}
                      onChange={this.onChangeCategory}
                      >
                    {
                        this.state.listCategories.map(item =>{
                            return (
                              <option key={item.id} value={item.id}> {item.name} </option>
                            )
                        })
                    }
                    </select>
                </div>

                <div className="form-group">
                  <label htmlFor="name">Product Name</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="name"
                    value={this.state.name}
                    onChange={this.onChangeName}
                    validations={[required, vname]}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="sku">SKU</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="sku"
                    value={this.state.sku}
                    onChange={this.onChangeSku}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="inpPrice">Input Price</label>
                  <Input
                    type="number"
                    className="form-control"
                    name="inpPrice"
                    step=".01"
                    value={this.state.inpPrice}
                    onChange={this.onChangeInpPrice}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="sellPrice">Sell Price</label>
                  <Input
                    type="number"
                    className="form-control"
                    name="sellPrice"
                    step=".01"
                    value={this.state.sellPrice}
                    onChange={this.onChangeSellPrice}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="quantity">Quantity</label>
                  <Input
                    type="number"
                    className="form-control"
                    name="quantity"
                    step=".01"
                    value={this.state.quantity}
                    onChange={this.onChangeQuantity}
                  />
                </div>               
                
                <div className="form-group">
                    <label htmlFor="unit">Unit</label>
                    <select
                      className="form-control"
                      name="unit"
                      value={this.state.unit}
                      onChange={this.onChangeUnit}
                      >
                    {
                        this.state.listUnit.map(item =>{
                            return (
                            <option key={item}>{item} </option>
                            )
                        })
                    }
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="picture">Picture</label>
                    <Input
                      type="file"
                      name="picture"
                      accept="image/*"
                      onChange={this.onChangePicture}
                    />                                        
                    <img alt="preview" src={
                      this.state.picturePreviewUrl === null ? 
                      "https://trentini.com.br/wp-content/uploads/2016/04/dummy-post-square-1-768x768.jpg" : this.state.picturePreviewUrl
                      } height="200px" width="200px"/>
                </div>

                <div className="form-group">
                  <button className="btn btn-primary btn-block">Add New Product</button>
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
      </div>
    );
  }
}
