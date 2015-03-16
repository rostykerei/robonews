(function($){
    var $window = $(window),

        $nav,
        $active,

        $indicator,
        $indicatorAnimation = {duration: 300, queue: false, easing: 'easeOutQuad'},
        $indicatorAnimationDelayed = $.extend({}, $indicatorAnimation, {delay: 90}),

        $html5history = !!(window.history && history.pushState),
        $currentState = {tab: null, story: null};

    $.app = $.app || {
        init: function() {
            $nav = $('#nav');
            $indicator = $nav.find('#indicator'),

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

            if ($nav.width() < $nav.prop('scrollWidth')) {
                $('#nav-bar .arrow').show();
            }

            $('#nav-bar .arrow').click(function() {
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
            $('#loader').fadeOut('slow', function() { $('#loader').remove() });
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

            $('#log').append("<hr/>[TAB: " + $state.tab + " | STORY: " + ($state.story ? $state.story : 'NONE') + "]");

            $currentState = $state;
        },
        _winResize: function() {

            $('#test').append($nav.innerWidth() + " / " + $nav[0].scrollWidth + "<hr/>");
            if ($nav.width() < $nav.prop('scrollWidth')) {
                $('#nav-bar .arrow').show();hhnk.nl
            }
            else {
                // todo this doesnt work if latest tab selected
                $('#nav-bar .arrow').hide();
            }

            setTimeout(function() {
                $.app.resetNavIndicator(false);
            }, 50);
        }
    };
})(jQuery);

$(document).ready(function () {
    $.app.init();
});
