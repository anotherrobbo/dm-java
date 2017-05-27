<#import "/common/application.ftl" as layout>
<@layout.dmLayout page='index'; section>
<#if section = "content">
<div class="container">
    <div class="row">
        <div class="col-lg-6">
            <form id="search-form">    
            <div class="form-group">
                <label class="control-label" for="un">Player Username:</label>
                <div class="input-group">
                    <input type="text" class="form-control has-error" id="un" placeholder="Username" required>
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="submit" id="submit">Search</button>
                    </span>
                </div>
            </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <span class="loading-spinner" style="display:none;"></span>
            <div id="results" class="list-group"></div>
        </div>
    </div>
</div>
</#if>
</@layout.dmLayout>