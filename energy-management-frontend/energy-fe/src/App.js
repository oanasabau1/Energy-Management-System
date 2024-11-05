import Navbar from "./components/navbar/Navbar";
import Login from "./pages/login/Login";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import AdminDashboard from "./pages/adminDashboard/AdminDashboard";
import UserDashboard from "./pages/userDashboard/UserDashboard";
import UserManagement from "./pages/userManagement/UserManagement";
import UpdateUser from "./pages/updateUser/UpdateUser";
import AddUser from "./pages/addUser/AddUser";
import DeviceManagement from "./pages/deviceManagement/DeviceManagement";
import AddDevice from "./pages/addDevice/AddDevice";
import UpdateDevice from "./pages/updateDevice/UpdateDevice";

function App() {
    return(
        <BrowserRouter>
        <div>
            <Navbar />
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path='/login' element={<Login />} />
                <Route path='/admin-dashboard' element={<AdminDashboard />} />
                <Route path='/devices' element={<DeviceManagement />} />
                <Route path='/device' element={<AddDevice />} />
                <Route path='/device/update/:deviceId' element={<UpdateDevice />} />
                <Route path='/users' element={<UserManagement />} />
                <Route path='/user' element={<AddUser />} />
                <Route path='/user/update/:userId' element={<UpdateUser />} />
                <Route path='/user/:userId/devices' element={<UserDashboard />} />
            </Routes>
        </div>
        </BrowserRouter>
    );
}

export default App;
