(function($){
    var $window = $(window),

        $nav,
        $active,

        $indicator,
        $indicatorAnimation = {duration: 300, queue: false, easing: 'easeOutQuad'},
        $indicatorAnimationDelayed = $.extend({}, $indicatorAnimation, {delay: 90}),

        $html5history = !!(window.history && history.pushState),
        $currentState = {tab: null, story: null},

        $masonryActive = false;

    $.app = $.app || {
        init: function() {
            $nav = $('#nav');
            $indicator = $nav.find('#indicator');

            $window.bind('load popstate statechange', $.app._stateChange);

            $('a.internal').click(function(e) {
                e.preventDefault();
                $.app.go($(this).attr('href'));
            });

            $nav.find('li').click(function(e) {
                e.preventDefault();
                var $link = $(this).find('a');
                $.app.go($link.attr('href'));
            });

            $window.resize($.app._winResize);

            var $navBarArrow = $('#nav-bar').find('.arrow');

            if ($nav.width() < $nav.prop('scrollWidth')) {
                $navBarArrow.show();
            }

            $navBarArrow.click(function() {
                $nav.animate({scrollLeft: $nav.scrollLeft() + ($(this).hasClass('left') ? -1 : 1) * $nav.width()}, 500);
            });
        },
        go: function(link) {
            if ($html5history) {
                window.history.pushState(null, null, link);
                $window.trigger('statechange');
            }
            else {
                window.location.hash = '#' + link;
            }
        },
        selectTab: function(id, animate) {
            $active = $nav.find('li.active').first();

            if($active.length == 0) {
                animate = false;
            }
            else {
                $active.removeClass('active');
            }

            $active = $nav.find('a[href="/' + id + '"]').parent();
            $active.addClass('active');

            $.app.resetNavIndicator(animate);
        },
        resetNavIndicator: function(animate) {
            var $scroll = $nav.scrollLeft();
            var $left = $active.position().left + $scroll,
                $right = $nav.width() - ($active.position().left + $active.outerWidth()) - $scroll;
            if (animate) {
                if ($left >= $indicator.position().left) {
                    $indicator.velocity({"left": $left}, $indicatorAnimationDelayed);
                    $indicator.velocity({"right": $right}, $indicatorAnimation);
                }
                else {
                    $indicator.velocity({"left": $left}, $indicatorAnimation);
                    $indicator.velocity({"right": $right}, $indicatorAnimationDelayed);
                }
            }
            else {
                $indicator.css("left", $left);
                $indicator.css("right", $right);
            }
        },
        showLoader: function() {
            $('#loader').remove();
            $('body').append($(document.createElement('div')).attr('id', 'loader'));
        },
        hideLoader: function() {
            $('#loader').fadeOut('250', function() { $('#loader').remove() });
        },
        _stateChange: function(e) {
            var $location = window.history.location || window.location;

            var $state;

            if ($html5history) {
                $state = {
                    tab: $location.pathname.length > 1 ? $location.pathname.substr(1) : 'top',
                    story: $location.search ? $location.search.substr(1) : null
                }
            }
            else {
                var $hash = $location.hash;
                if ($location.pathname != '/') {
                    $location.href = "/" + $hash;
                    return;
                }

                if ($hash.length > 2) {
                    $hash = $hash.substr(2); // removing leading '#/'
                    var $qPos = $hash.indexOf('?');
                    if ($qPos > -1) {
                        $state = {
                            tab: $hash.substr(0, $qPos),
                            story: $qPos < $hash.length-1 ? $hash.substr($qPos+1) : null
                        }
                    }
                    else {
                        $state = {
                            tab: $hash,
                            story: null
                        }
                    }
                }
                else {
                    $state = {
                        tab: 'top',
                        story: null
                    }
                }
            }

            if ($currentState.tab == $state.tab && $currentState.story == $state.story) {
                return;
            }

            var $tabId = ($state.tab == 'top' ? '' : $state.tab),
                $link = $nav.find('a[href="/' + $tabId + '"]');

            if ($link.length === 0) {
                if ($html5history) {
                    window.history.replaceState(null, null, '/');
                    $window.trigger('statechange');
                }
                else {
                    $location.href = '/';
                }
                return;
            }

            if (e.type != 'load') {
                $.app.selectTab($tabId, true);
            }
            else {
                setTimeout(function(){
                    $.app.selectTab($tabId, false);
                }, 50);
            }

            console.debug("TAB: " + $state.tab + " | STORY: " + ($state.story ? $state.story : 'NONE'));

            if (! $state.story) {
                $.app._loadSection($state.tab)
            }

            $currentState = $state;
        },
        _loadSection: function(section) {

            var $page = $('#p');
            $page.html('');

            if ($masonryActive) {
                $page.masonry('destroy');
                $masonryActive = false;
            }

            $page.css('opacity', '0');

            $.app.showLoader();

            $.getJSON("/data/" + section +".json")
                .done(function( json ) {
                    document.title = 'Robonews.io - ' + json.title;

                    var i = 0;
                    $.each( json.cards, function( card ) {
                        $page.append($.app._renderCard( this, i++ ));
                    });

                    $page.imagesLoaded( function(){
                        $page.masonry({
                            itemSelector : '.card'
                        });

                        $masonryActive = true;

                        $.app.hideLoader();
                        $.app._showCards();

                        $page.addClass('showPage');

                        setTimeout(function () {
                            $page.css('opacity', '1');
                            $page.removeClass('showPage');
                        }, 250);
                    });
                })
                .fail(function( jqxhr, textStatus, error ) {
                    var err = textStatus + ", " + error;
                    console.log( "Request Failed: " + err );
                    // todo error?
                    $.app.hideLoader();
                });
        },
        _showCards: function() {
            var windowHeight = $(window).height(),
                scrollable = $((navigator.userAgent.toLowerCase().indexOf('webkit') != -1) ? 'body' : 'html'),
                initScreenBottom = scrollable.scrollTop() + windowHeight,
                cards = $(".card");

            var sc = function(){
                var bottom = scrollable.scrollTop() + windowHeight - 50;

                cards.each(function(){
                    var card = $(this);
                    if (card.hasClass('visible')){
                        return;
                    }

                    if (card.offset().top < bottom){
                        card.addClass('visible showCard');
                    }
                });
            };

            cards.each(function() {
                $(this).addClass(($(this).offset().top <= initScreenBottom) ? 'visible' : 'hidden');
            });

            $(window).scroll(sc);
            sc();

            /* todo
            $(window).resize(function(e){
                windowHeight = e.currentTarget.innerHeight;
            });*/
        },
        _winResize: function() {
            if ($nav.width() < $nav.prop('scrollWidth')) {
                $('#nav-bar .arrow').show();
            }
            else {
                // todo this doesnt work if latest tab selected
                $('#nav-bar .arrow').hide();
            }

            setTimeout(function() {
                $.app.resetNavIndicator(false);
            }, 50);
        },
        _renderCard: function( card, position ) {
            if ( card.type == 'hero') {
                return $.app._renderTemplate('tpl-card-hero', card);
            }
            else {
                return '<div class="card"><div>'+
                        //'<div  style="border: solid 1px red; margin: 0; background: red; height: 400px; width: 50%;"></div>'
                    '<img style="display: block;" width="50%" height="' + card.height + '" src="http://lorempixel.com/280/' + card.height + '/?' + (Math.random() * 10000) +'"/>' +
                    '</div></div>';
            }
        },
        _renderTemplate: function( templateId, data ) {
            var template = $('#' + templateId).html();

            for (var key in data) {
                template = template.replace("${" + key + "}", data[key]);
            }

            return template;
        }
    };
})(jQuery);

$(document).ready(function () {
    $.app.init();
});
