import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import authService from "../../services/auth.service";
import AuthService from "../../services/auth.service";

export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: { username: "" }
    };
    this.editUser = this.editUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) this.setState({ redirect: "/home" });
    this.setState({ currentUser: currentUser, userReady: true })
  }

  editUser(id){
    this.props.history.push(`/user/${id}`);
  }

  deleteUser(id){
    authService.deleteUser(id).then(res => {
      AuthService.logout();
        this.props.history.push('/login');
    })
  }


  render() {
    if (this.state.redirect) {
      return <Redirect to={this.state.redirect} />
    }

    const { currentUser } = this.state;

    return (
      <div className="jumbotron">
        {(this.state.userReady) ?
        <div>
          <h3>
            <strong>{currentUser.username}</strong> Profile
            
          </h3>
          <button onClick={ () => this.editUser(currentUser.id)} style={{marginLeft: "10px"}} className="btn btn-success">Update </button> 
            {currentUser.roles.includes("ROLE_ADMIN") &&
              <button onClick={ () => this.deleteUser(currentUser.id)} style={{marginLeft: "10px"}} className="btn btn-danger">Delete </button>
            }
          

          <p>
          <strong>Token:</strong>{" "}
          {currentUser.accessToken.substring(0, 20)} ...{" "}
          {currentUser.accessToken.substr(currentUser.accessToken.length - 20)}
        </p>
        <p>
          <strong>Id:</strong>{" "}
          {currentUser.id}
        </p>
        <p>
          <strong>Email:</strong>{" "}
          {currentUser.email}
        </p>
        <strong>Authorities:</strong>
        <ul>
          {currentUser.roles &&
            currentUser.roles.map((role, index) => <li key={index}>{role}</li>)}
        </ul>
        
      </div>: null}
      </div>
    );
  }
}
