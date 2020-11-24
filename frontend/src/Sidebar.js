import Filters from "./sidebar/Filters.js"
import HistoryList from "./sidebar/HistoryList.js"
import GenerateMapForm from "./sidebar/GenerateMapForm.js"

function Sidebar() {
  return (
    <div id="sidebar-container" class="sidebar-expanded d-none d-md-block">
      <ul class="list-group sidebar-list-expanded overflow-auto" id="sidebar-list">
        <li class="list-group-item sidebar-separator-title text-muted d-flex align-items-center menu-collapsed">
          <small>MAIN MENU</small>
        </li>
        <a href="#./" data-toggle="sidebar-collapse"
          class="bg-light list-group-item list-group-item-action d-flex align-items-center">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span id="collapse-icon" class="fa fa-2x mr-3"></span>
            <span id="collapse-text" class="menu-collapsed">Collapse</span>
          </div>
        </a>
        <a href="#home-submenu" data-toggle="collapse" aria-expanded="false"
          class="bg-light list-group-item list-group-item-action flex-column align-items-start"
          id="generate-sidebar-button">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span class="fa fa-plus fa-fw mr-3"></span>
            <span class="menu-collapsed">Generate</span>
            <span class="submenu-icon ml-auto"></span>
          </div>
        </a>
        <div id='home-submenu' class="collapse sidebar-submenu card">
          <article class="card-group-item">
            <header class="card-header">
              <h6 class="title">Generate Maps</h6>
            </header>
            <GenerateMapForm></GenerateMapForm>
          </article>
        </div>
        <a href="#filter-submenu" data-toggle="collapse" aria-expanded="false"
          class="bg-light list-group-item list-group-item-action flex-column align-items-start">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span class="fa fa-filter fa-fw mr-3"></span>
            <span class="menu-collapsed">Filters</span>
            <span class="submenu-icon ml-auto"></span>
          </div>
        </a>
        <div id='filter-submenu' class="collapse sidebar-submenu card">
          <Filters></Filters>
        </div>
        <a href="#history-submenu" data-toggle="collapse" aria-expanded="false"
          class="bg-light list-group-item list-group-item-action flex-column align-items-start"
          id="history-sidebar-button">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span class="fa fa-history fa-fw mr-3"></span>
            <span class="menu-collapsed">History</span>
            <span class="submenu-icon ml-auto"></span>
          </div>
        </a>
        <div id='history-submenu' class="collapse sidebar-submenu card">
          <article class="card-group-item">
            <HistoryList></HistoryList>
          </article>
        </div>
      </ul>
    </div>
  );
}

export default Sidebar;
