import './Navbar.css';
import { MdEnergySavingsLeaf } from "react-icons/md";

const Navbar = () => {

    return (
        <div className="navbar">
            <div className="nav-logo">
                <MdEnergySavingsLeaf size={80} className="nav-icon"/>
                <p> Energy Management System </p>
            </div>
        </div>
    );
};

export default Navbar;